package com.example.stockapp.search

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.stockapp.R
import com.example.stockapp.databinding.FragmentOverviewBinding
import com.example.stockapp.databinding.FragmentSearchBinding
import com.example.stockapp.databinding.FragmentStockBinding
import com.example.stockapp.domain.StockDataModel
import com.example.stockapp.overview.OverviewAdapter
import com.example.stockapp.stock.StockAdapter
import com.example.stockapp.stock.StockViewModel
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.list_item_car.view.*
import java.util.ArrayList
import java.util.zip.Inflater

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

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

        viewModel.loadData()

        binding.countryList.layoutManager = GridLayoutManager(activity, 2)
        binding.countryList.adapter = CountryAdapter(viewModel.displayList, this.requireContext())

        setHasOptionsMenu(true)
        return binding.root
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



