package com.example.stockapp.watchlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stockapp.databinding.FragmentSearchBinding
import com.example.stockapp.domain.StockDataModel
import kotlinx.android.synthetic.main.fragment_search.*
import android.util.Log
import androidx.lifecycle.Observer
import com.example.stockapp.R
import com.example.stockapp.databinding.FragmentWatchListBinding
import com.example.stockapp.network.StockApiService
import com.example.stockapp.network.StockApiServiceBuilder
import com.example.stockapp.network.StockSearchProperty
import com.example.stockapp.search.CountryAdapter
import kotlinx.android.synthetic.main.fragment_watch_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WatchListFragment : Fragment() {


    public var stocksNameList: MutableList<String> = ArrayList()

    private val alphavantageFreeApiKey = "CITUC4CO27CTCF4D"

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

        viewModel.loadData()

        binding.countryList.layoutManager = GridLayoutManager(activity, 2)
        binding.countryList.adapter = CountryAdapter(viewModel.displayList, this.requireContext())

        // AutoCompleteTextView
        //      val autotextView = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)



        //   val languages = resources.getStringArray(R.array.Languages)


//        val adapter = ArrayAdapter<String>(this.activity,
////            android.R.layout.simple_dropdown_item_1line, stocksNameList)
////        binding.autoCompleteTextView.setAdapter(adapter)


        //adapter.notifyDataSetChanged()


        viewModel.searchResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.i("ccccc","ccccc observe - searchResponse changed")
//                stocksNameList.clear()
//                adapter.clear()
//                stocksNameList = viewModel.getStocksNameList()
//                adapter.addAll(stocksNameList)
//                adapter.notifyDataSetChanged()
            }
        })

        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        binding.autoCompleteTextView.threshold = 1
        //   binding.autoCompleteTextView.requestFocus()
        binding.autoCompleteTextView.clearFocus()
        binding.autoCompleteTextView.hint = "Search here..."

        binding.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            @Override
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            @Override
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                getSearchResults(binding.autoCompleteTextView.text.toString(), alphavantageFreeApiKey, binding)

                Log.i("bbbbb","bbbbb  text entered: " + (binding.autoCompleteTextView.text.toString()))
                Log.i("bbbbb", "bbbbb   stocks size is" + (viewModel.searchResponse.value?.metaData?.size))
                Log.i("bbbbb", "bbbbb   stocks name list: " + (viewModel.getStocksNameList()))

//                adapter.addAll(stocksNameList)
//                adapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable) {}
        })


        // Set an item click listener for auto complete text view
        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            // Display the clicked item using toast
            Toast.makeText(this.context,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }

        // Set a dismiss listener for auto complete text view
        binding.autoCompleteTextView.setOnDismissListener {
            Toast.makeText(this.context,"setOnDismissListener-Suggestion closed.",Toast.LENGTH_SHORT).show()
        }

        // Set a focus change listener for auto complete text view
        binding.autoCompleteTextView.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            //            if(b){
//                // Display the suggestion dropdown on focus
//                binding.autoCompleteTextView.showDropDown()
//            }
//            Toast.makeText(this.context,"onFocusChangeListener",Toast.LENGTH_SHORT).show()
        }


        setHasOptionsMenu(true)
        return binding.root
    }

    fun getSearchResults(
        keywords: String,
        apikey: String,
        binding: FragmentWatchListBinding
    ) {
        val request = StockApiServiceBuilder.buildService(StockApiService::class.java)
        val call = request.getSearchResults(keywords, apikey)

        call.enqueue(object : Callback<StockSearchProperty> {

            override fun onResponse(
                call: Call<StockSearchProperty>,
                response: Response<StockSearchProperty>
            ) {
                if (response.isSuccessful) {
                    stocksNameList.clear()
                    val str = response.body()?.metaData
                    for (s in str.orEmpty()) {
                        Log.i("metadata",s.name)
                        println("aaaaaaaa printing the metaddata: " +s.name)
                        stocksNameList.add(s.name)
                    }

                    val adapter = ArrayAdapter<String>(context,
                        android.R.layout.simple_dropdown_item_1line, stocksNameList)
                    binding.autoCompleteTextView.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<StockSearchProperty>, t: Throwable) {
                Toast.makeText(context,"onFocusChangeListener",Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        if(searchItem != null){
            val searchView = searchItem.actionView as SearchView
            val editext = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            editext.hint = "Search here..."

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.displayList.clear()
                    if(newText!!.isNotEmpty()){
                        val search = newText.toLowerCase()
                        viewModel.countries.forEach {
                            if(it.toLowerCase().startsWith(search)){
                                viewModel.displayList.add(it)
                            }
                        }
                    }else{
                        viewModel.displayList.addAll(viewModel.countries)
                    }
                    country_list.adapter?.notifyDataSetChanged()
                    return true
                }

            })
        }
        return super.onCreateOptionsMenu(menu, inflater)

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



