package com.roeyc.stockapp.stockinfo

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.roeyc.stockapp.R

import com.roeyc.stockapp.databinding.FragmentStockInfoBinding
import com.roeyc.stockapp.domain.StockDataModel
import com.roeyc.stockapp.domain.UserStocksDataModel


/**
 * A simple [Fragment] subclass.
 * Use the [StockInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StockInfoFragment : Fragment() {

    // set it with greater then 3 , in that case the default will be hidding add button
    private var amountOfUserStocks = 4


    private val viewModel: StockInfoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val stockData = StockInfoFragmentArgs.fromBundle(arguments!!).selectedStock

        ViewModelProviders.of(this, StockInfoViewModel.Factory(stockData, activity.application))
            .get(StockInfoViewModel::class.java)
    }

    private lateinit var binding: FragmentStockInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStockInfoBinding.inflate(inflater)
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = viewModel


        viewModel.ss.observe(viewLifecycleOwner, Observer<StockDataModel> { stock ->
            stock?.apply {
                viewModel.setStockProperty(stock)
            }

            // it called twice - for solving the bug that button still appear - i have to check also stock is not null
            if (stock != null && amountOfUserStocks < 3) {
                binding.addStockButton.visibility = View.VISIBLE
            } else if (stock != null && amountOfUserStocks > 2) {
                Toast.makeText(this.context,"Your stock list is full, delete some to add new" , Toast.LENGTH_LONG).show()
            }
        })

        // in the init() phase of the viewmodel - should be called first (there is an option to set
        // the addStockButton.visibility to invisiable in here also
        viewModel.userStocks.observe(viewLifecycleOwner, Observer<List<UserStocksDataModel>> { userStocks ->
            amountOfUserStocks = userStocks.size
        })

        binding.addStockButton.setOnClickListener {
            viewModel.addToStockListClicked()
            binding.addStockButton.isEnabled = false
            Toast.makeText(this.context,"The stock is in your list" , Toast.LENGTH_LONG).show()
        }

        viewModel.addToStockList.observe(viewLifecycleOwner, Observer<Boolean> {
            it.let {
                viewModel.addSelectedStockToStockList()
        //        viewModel.addToStockListCompleted()
            }

        })


        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      //  addAnimationOperations()
        super.onViewCreated(view, savedInstanceState)
    }





    // animations using ConstraintLayout  (via second layout)
    // https://proandroiddev.com/creating-awesome-animations-using-constraintlayout-and-constraintset-part-i-390cc72c5f75
    fun addAnimationOperations() {
        var set = false
        val constraint1 = ConstraintSet()
        constraint1.clone(binding.root)
        val constraint2 = ConstraintSet()
        constraint2.clone(context, R.layout.fragment_stock_info_second)
        // first way - option to do it without second layout
        // https://robinhood.engineering/beautiful-animations-using-android-constraintlayout-eee5b72ecae3
        //constraint2.clone(binding.root)
        //constraint2.centerVertically(binding.imageView.id, 0)

        binding.imageView.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val transition = AutoTransition()
                transition.duration = 5000
                TransitionManager.beginDelayedTransition(binding.root, transition)

              // CHECK - second way - without creating second layout for animation
              // https://medium.com/@ashwinbhskr/a-better-way-to-change-view-dimensions-programatically-36e56b61a9b9
              // binding.viewModel!!.dimensionsHelper.notifyChange(200, 200)

                val constraint = if(set) constraint1 else constraint2
                constraint.applyTo(binding.root)
                set = !set
            }
        }
    }
}
