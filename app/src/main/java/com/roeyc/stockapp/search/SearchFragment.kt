package com.roeyc.stockapp.search

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.roeyc.stockapp.R
import com.roeyc.stockapp.databinding.FragmentSearchBinding
import com.roeyc.stockapp.domain.StockDataModel
import com.roeyc.stockapp.stocksList.Stocks
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.lang.reflect.Type
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    val stocksInstance = Stocks.instance
    private var recentSearchViewModelAdapter: RecentSearchAdapter? = null
    private var subscribe: Disposable? = null

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

        binding = FragmentSearchBinding.inflate(inflater)
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.viewModel = viewModel

//        val prefs: SharedPreferences =
//            context!!.getSharedPreferences("test", MODE_PRIVATE)
        // save to shared preferences
      //  stocksInstance!!.stocks?.let { saveHashmapToSharedPreferences(prefs, it) }

        //   val sharedPreferences: SharedPreferences = context!!.getSharedPreferences(sharedPrefFile,
        //       Context.MODE_PRIVATE)
        //   val editor:SharedPreferences.Editor =  sharedPreferences.edit()

        binding.autoComplete.hint = "Search here ..."
        binding.autoComplete.setHintTextColor(R.color.DeepPink.toInt())
        binding.autoComplete.threshold = 1
        binding.autoComplete.setDropDownBackgroundResource(R.color.AntiqueWhite)
        // binding.autoComplete.setDropDownVerticalOffset(1);

        binding.autoComplete.onRightDrawableClicked {
            it.text.clear()
        }

        val results = getSearchResultsFromStockList()
        val adapter =
            SearchAdapter(context!!, android.R.layout.simple_expandable_list_item_2, results)
        binding.autoComplete.setAdapter(adapter)

        binding.autoComplete.setOnItemClickListener() { parent, _, position, id ->
            var selectedStock = parent.adapter.getItem(position) as SearchResultDao?
            selectedStock = adapter.editStockName(selectedStock)
            viewModel.navigateToStockInfo(selectedStock)
            binding.autoComplete.text.clear()
        }

        viewModel.navigateToStockInfoFragment.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(SearchFragmentDirections.actionSearchFragmentToStockInfoFragment(it))
                viewModel.navigateToStockInfoComplete()
            }
        })

        viewModel.stocklist.observe(viewLifecycleOwner, Observer<List<StockDataModel>> { stocks ->
            stocks?.apply {
                recentSearchViewModelAdapter?.stocks = stocks
            }
        })

        recentSearchViewModelAdapter = RecentSearchAdapter()
        binding.recentSearchRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.recentSearchRecyclerview.adapter = recentSearchViewModelAdapter
        setupItemClick()

     //   binding.recentSearchRecyclerview.setOn

        // move search to ToolBar
        setHasOptionsMenu(false)
        return binding.root
    }

    private fun setupItemClick() {
        subscribe = recentSearchViewModelAdapter!!.clickEvent.subscribe({
            Toast.makeText(this.context, "Clicked on $it", Toast.LENGTH_LONG).show()
        })

    }

    private fun createItemClickObservable(): Observable<StockDataModel> {
            return Observable.create { emmiter ->
             //   binding.recentSearchRecyclerview.addOnItemTouchListener()
              //  onItemClicked()
            }
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
                Toast.makeText(
                    this.context,
                    "setOnDismissListener-Suggestion closed.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Set a focus change listener for auto complete text view
            autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
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
        for ((key, value) in stocksInstance!!.stocks!!) {
            allStocks.add(SearchResultDao(key, value))
        }

        return allStocks
    }

    override fun onDestroy() {
        super.onDestroy()
        subscribe?.dispose()
    }

    private fun saveHashmapToSharedPreferences(
        prefs: SharedPreferences,
        hashmap: HashMap<String, String>
    ) { //create test hashmap
        //convert to string using gson
        val gson = Gson()
        val hashMapString = gson.toJson(hashmap)

        //save in shared prefs
        prefs.edit().putString("hashString", hashMapString).apply()
    }

    private fun getHashmapFromSharedPreferences(prefs: SharedPreferences): HashMap<String, String> {
        //get from shared prefs
        val storedHashMapString = prefs.getString("hashString", "oopsDintWork")
        val type: Type =
            object : TypeToken<HashMap<String?, String?>?>() {}.getType()
        val gson = Gson()
        val stocksHashMap: HashMap<String, String> =
            gson.fromJson(storedHashMapString, type)
        return stocksHashMap
    }

}

