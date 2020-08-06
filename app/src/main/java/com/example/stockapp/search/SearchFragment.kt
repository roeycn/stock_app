package com.example.stockapp.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.stockapp.R
import com.example.stockapp.databinding.FragmentSearchBinding
import com.example.stockapp.domain.StockDataModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    lateinit var stockHashMap : HashMap<String,String>

    private val viewModel: SearchViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, SearchViewModel.Factory(activity.application))
            .get(SearchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentSearchBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        stockHashMap = viewModel.readFileAsLinesUsingBufferedReader(context)

        binding.autoComplete.hint = "Search here ..."
        binding.autoComplete.setHintTextColor(R.color.DeepPink.toInt())
        binding.autoComplete.threshold = 1
        binding.autoComplete.setDropDownBackgroundResource(R.color.AntiqueWhite)
       // binding.autoComplete.setDropDownVerticalOffset(1);

        binding.autoComplete.onRightDrawableClicked {
            it.text.clear()
        }

        val results = getSearchResultsFromStockList()
        val adapter = SearchAdapter(context!!, android.R.layout.simple_expandable_list_item_2, results)
        binding.autoComplete.setAdapter(adapter)

        binding.autoComplete.setOnItemClickListener() { parent, _, position, id ->
            var selectedStock = parent.adapter.getItem(position) as SearchResultDao?
            selectedStock = adapter.editStockName(selectedStock)
            viewModel.navigateToStockInfo(selectedStock)
            binding.autoComplete.text.clear()
        }

        viewModel.navigateToStockInfoFragment.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                this.findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToStockInfoFragment(it))
                viewModel.navigateToStockInfoComplete()
            }
        })

        // move search to ToolBar
        setHasOptionsMenu(false)
        return binding.root
    }

    private fun getSearchResultsFromStockList(): List<SearchResultDao> {
     val combinedData = combineStockSymboleAndName()
     return combinedData
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        // Inflate the search menu action bar.
        inflater.inflate(R.menu.search_stocks_menu, menu)

        // Get the search menu
        val searchItem = menu.findItem(R.id.menu_search_stocks)

        if (searchItem != null) {
            // Get the autoCompleteText object
            val autoCompleteTextView = searchItem.actionView as AutoCompleteTextView
            autoCompleteTextView.hint = "Search here ..."
            autoCompleteTextView.setHintTextColor(R.color.DeepPink.toInt())
            autoCompleteTextView.threshold = 1
            autoCompleteTextView.setDropDownBackgroundResource(R.color.AntiqueWhite)

            val results = getSearchResultsFromStockList()
            // using the adapter class
            val adapter = SearchAdapter(context!!, android.R.layout.simple_list_item_2, results)

            // creating the adapter here
          //   val stockNameList = ArrayList(stockHashMap.values)
          //    val adapter = ArrayAdapter<String>(context,
          //    android.R.layout.simple_dropdown_item_1line, stockNameList)

            autoCompleteTextView.setAdapter(adapter)

            //  adapter.notifyDataSetChanged()


            autoCompleteTextView.setOnItemClickListener() { parent, _, position, id ->
                val selectedStock = parent.adapter.getItem(position) as SearchResultDao?
                autoCompleteTextView.setText(selectedStock?.stockName)
            }


            autoCompleteTextView.addTextChangedListener(object : TextWatcher {
                @Override
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    Log.d("beforeTextChanged", s.toString());
                }

                @Override
                override fun onTextChanged(
                    s: CharSequence, start: Int, before: Int,
                    count: Int
                ) {
                   // getSearchResults(searchView.text.toString(), alphavantageFreeApiKey, searchView)
                   // getSearchResultsFromStockList(searchView)
                  //  val results = getSearchResultsFromStockList(autoCompleteTextView)
                    Log.d("onTextChanged", s.toString());
                }

                @Override
                override fun afterTextChanged(s: Editable) {
                    Log.d("afterTextChanged", s.toString());
                }
            })

            // Set an item click listener for auto complete text view
//            searchView.onItemClickListener = AdapterView.OnItemClickListener{
//                    parent,view,position,id->
//                val selectedItem = parent.getItemAtPosition(position).toString()
//                // Display the clicked item using toast
//                Toast.makeText(this.context,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
//            }

//            searchView.onItemClickListener = AdapterView.OnItemClickListener{
//                                    parent,view,position,id->
//                //val selectedItem = parent.getItemAtPosition(position).toString()
//                // Display the clicked item using toast
//                //Toast.makeText(this.context,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
//                val selectedPoi = parent.adapter.getItem(position) as SearchResultDao?
//                searchView.setText(selectedPoi?.stockName)
//           }


            // If you need to perform some action after auto complete dropdown has been dismissed
            autoCompleteTextView.setOnDismissListener {
                Toast.makeText(this.context,"setOnDismissListener-Suggestion closed.",Toast.LENGTH_SHORT).show()
            }

            // Set a focus change listener for auto complete text view
            autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener{
                    view, b ->
                //            if(b){
//                // Display the suggestion dropdown on focus
//                binding.autoCompleteTextView.showDropDown()
//            }
//            Toast.makeText(this.context,"onFocusChangeListener",Toast.LENGTH_SHORT).show()
            }

            return super.onCreateOptionsMenu(menu, inflater)

        }
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

    private fun combineStockSymboleAndName(): List<SearchResultDao> {

        val allStocks = mutableListOf<SearchResultDao>()
        for ((key, value) in stockHashMap) {
            allStocks.add(SearchResultDao(key, value))
        }

        return allStocks
    }


}



