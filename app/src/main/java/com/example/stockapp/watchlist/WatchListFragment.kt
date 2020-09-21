package com.example.stockapp.watchlist

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dragdroprecyclerview.ItemMoveCallbackListener
import com.example.stockapp.R
import com.example.stockapp.databinding.FragmentWatchListBinding
import com.example.stockapp.domain.UserStocksDataModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WatchListFragment : Fragment(), OnStartDragListener {

    lateinit var adapter: WatchListAdapter
    lateinit var touchHelper: ItemTouchHelper
    var actionMode: ActionMode? = null

    private val viewModel: WatchListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, WatchListViewModel.Factory(activity.application))
            .get(WatchListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentWatchListBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        adapter = WatchListAdapter(this, viewModel)


        // in case i delete an item - then this called again and causing crash on bindviewholder -it getting an array out of index
        viewModel.userStocks.observe(viewLifecycleOwner, Observer<List<UserStocksDataModel>> { userStocks ->
            userStocks?.apply {
                // adapter.setUserStocks(userStocks)
                // need to check how to handle without it
                adapter.submitList(userStocks)

                binding.fabAddStock.isEnabled = userStocks.size < 3

                if (!binding.fabAddStock.isEnabled) {
                    binding.fabAddStock.visibility = View.INVISIBLE
                    binding.stockListTitle.text = String.format(
                        context!!.getString(R.string.stock_list_full_title))
                } else {
                    binding.fabAddStock.visibility = View.VISIBLE
                    binding.stockListTitle.text = String.format(
                        context!!.getString(R.string.stock_list_title), userStocks.size)
                }

                binding.totalValue.text = String.format(
                    context!!.getString(R.string.total_value), viewModel.calculateTotalValue(userStocks).toString())
            }
        })

       // val userStocks = viewModel.userStocks.value
       // adapter.setUserStocks(userStocks!!)
       // adapter.submitList(userStocks)
//        binding.totalValue.text = String.format(
//            context!!.getString(R.string.total_value), viewModel.calculateTotalValue(userStocks).toString())

   //     adapter.setUserStocks(viewModel.userStocks.value!!)

        val callback: ItemTouchHelper.Callback = ItemMoveCallbackListener(context!!, adapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.userStocksList)
        binding.userStocksList.adapter = adapter

        val manager = GridLayoutManager(activity, 1)
        binding.userStocksList.layoutManager = manager

        // possible to declare what to do in swipe right or left with:
//        binding.userStocksList.setOnTouchListener(object : OnSwipeTouchListener(){
//            override fun onSwipeLeft() {
//                Log.e("ViewSwipe", "Left")
//                super.onSwipeLeft()
//            }
//        });

        binding.confirmSelection.setOnClickListener() {
            viewModel.sendYourPortfolioClicked()
        }

        viewModel.mSendYourPortfolio.observe(viewLifecycleOwner, Observer {
            it?.let {
                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.send_your_portfolio_dailog_title)
                    .setMessage(R.string.send_your_portfolio_dailog_subtitle)
                    .setPositiveButton("YES") { dialog, which ->
                        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                        //the stock starting price - 1000$ is set when user add it to his list
                       // adapter.setUserStocks(viewModel.setStartingStocksPrice(adapter.mUserStocks))

                        binding.info.text = String.format(
                            context!!.getString(R.string.your_portfolio_saved_on), currentDate, currentTime)
                        binding.info.visibility = View.VISIBLE
                    }
                    .setNegativeButton("No") {dialog, which ->
                    }
                    .show()
            }
        })




        binding.fabAddStock.setOnClickListener() {
            viewModel.navigateSearchFragment()
        }

        viewModel.navigateToSearch.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(WatchListFragmentDirections.actionWatchListFragmentToSearchFragment())
                viewModel.navigateSearchFragmentComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }


    companion object {
        var isMultiSelectOn = false
        val TAG = "MainActivity"
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        touchHelper.startDrag(viewHolder!!)
    }
}



