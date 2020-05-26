package com.example.qaapp.stock

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qaapp.R
import com.example.qaapp.databinding.FragmentStockBinding
import com.example.qaapp.domain.StockDataModel

class StockFragment : Fragment() {

    private val viewModel: StockViewModel by lazy {
        //  ViewModelProviders.of(this).get(StockViewModel::class.java)

        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, StockViewModel.Factory(activity.application))
            .get(StockViewModel::class.java)
    }

    /**
     * RecyclerView Adapter for converting a list of Video to cards.
     */
    private var viewModelAdapter: StockAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.stocklist.observe(viewLifecycleOwner, Observer<List<StockDataModel>> { stocks ->
            stocks?.apply {
                viewModelAdapter?.stocks = stocks
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        //return inflater.inflate(com.example.qaapp.R.layout.fragment_mars, parent, false);
        var binding = FragmentStockBinding.inflate(inflater)

        binding.setLifecycleOwner(this)

        binding.viewModel = viewModel


        viewModelAdapter = StockAdapter(StockClick {
            // When a video is clicked this block or lambda will be called by DevByteAdapter

            // context is not around, we can safely discard this click since the Fragment is no
            // longer on the screen
            val packageManager = context?.packageManager ?: return@StockClick

            // Try to generate a direct intent to the YouTube app
//            var intent = Intent(Intent.ACTION_VIEW, it.launchUri)
//            if (intent.resolveActivity(packageManager) == null) {
//                // YouTube app isn't found, use the web url
//                intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
//            }
//            startActivity(intent)
        })

        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }

        binding.searchButton.setOnClickListener() {
            Toast.makeText(
                this.context,
                "search button clicked with: " + viewModel.getText().value,
                Toast.LENGTH_LONG
            ).show()
            viewModel.startSearch()
        }

        viewModel.searchButtonClicked.observe(viewLifecycleOwner, Observer {
            it?.let {
                // viewModel.getSearchResults(viewModel.editTextData.toString(), "demo")
                viewModel.getSearchResults(viewModel.getText().value.toString(), "demo")
                viewModel.stopSearch()
            }
        })

        viewModel.editTextData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(
                    this.context,
                    "edit text changed to: " + viewModel.getText().value,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return binding.root
    }

    /**
     * Click listener for Videos. By giving the block a name it helps a reader understand what it does.
     *
     */
    class StockClick(val block: (StockDataModel) -> Unit) {
        /**
         * Called when a stock is clicked
         *
         * @param stock the stock that was clicked
         */
        fun onClick(stock: StockDataModel) = block(stock)
    }

}

