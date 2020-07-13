package com.example.stockapp.overview

import android.content.Intent
import com.example.stockapp.R
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stockapp.SecondActivity
import com.example.stockapp.database.Cars
import com.example.stockapp.databinding.FragmentOverviewBinding


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OverviewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.@Override
public boolean onCreateOptionsMenu(Menu menu) {
getMenuInflater().inflate(R.menu.search_action_bar_menu, menu);

Activity activity = this;
SearchManager searchManager =
(SearchManager) getSystemService(Context.SEARCH_SERVICE);
SearchView searchView = (SearchView) MenuItemCompat
.getActionView(menu.findItem(R.id.action_search));

searchView.setSearchableInfo(
searchManager.getSearchableInfo(getComponentName()));
searchView.setQueryHint("Search for users...");
String [] columNames = { SearchManager.SUGGEST_COLUMN_TEXT_1 };
int [] viewIds = { android.R.id.text1 };
CursorAdapter adapter = new SimpleCursorAdapter(this,
android.R.layout.simple_list_item_1, null, columNames, viewIds);
searchView.setSuggestionsAdapter(adapter);


searchView.setOnSuggestionListener(getOnSuggestionClickListener());
searchView.setOnQueryTextListener(getOnQueryTextListener(activity, adapter));

return true;
}
 */
class OverviewFragment : Fragment() {

    private val viewModel: OverviewModel by lazy {
        ViewModelProviders.of(this).get(OverviewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        //        val binding: FragmentOverviewBinding = DataBindingUtil.inflate(
//            inflater, R.layout.fragment_overview, container, false)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        val adapter = OverviewAdapter(OverviewAdapter.OnClickListener {
            viewModel.displayDetails(it)
        })
        binding.carList.adapter = adapter

        viewModel.navigateToSelectedProperty.observe(this, Observer {
            if ( null != it ) {
                this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
                viewModel.displayDetailsComplete()
            }
        })

        val manager = GridLayoutManager(activity, 1)
        binding.carList.layoutManager = manager


        val car1 = Cars("aaaa", "11.11")
        val car2 = Cars("bbbb", "22.22")
        val car3 = Cars("cccc", "33.33")
        val car4 = Cars("dddd", "44.44")
        val car5 = Cars("eeee", "55.55")
        val car6 = Cars("ffff", "66.66")
        val car7 = Cars("gggg", "77.77")
        val car8 = Cars("hhhh", "88.88")
        val car9 = Cars("iiii", "99.99")
        val car10 = Cars("jjjj", "111.111")
        val car11 = Cars("kkkk", "222.222")
        val car12 = Cars("llll", "333.333")

        val carsListOfEight = mutableListOf<Cars>(car1,car2,car3,car4,car5,car6,car7,car8, car9, car10, car11, car12)

        viewModel.setCarCompany(carsListOfEight)

        viewModel.mCarCompany.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        binding.button5.setOnClickListener() {
            viewModel.navigateMarsFragment()
        }

        viewModel.navigateToMars.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToMarsFragment())
                viewModel.navigateMarsFragmentComplete()
            }
        })

        binding.goToSecondActivity.setOnClickListener() {
            val intent = Intent(activity, SecondActivity::class.java)
            startActivity(intent)
        }





        binding.stocks.setOnClickListener() {
            viewModel.navigateStockFragment()
        }

        viewModel.navigateToStock.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToStockFragment())
                viewModel.navigateStockFragmentComplete()
            }
        })

        binding.searchButton.setOnClickListener() {
            viewModel.navigateSearchFragment()
        }

        viewModel.navigateToSearch.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToSearchFragment())
                viewModel.navigateSearchFragmentComplete()
            }
        })

        binding.watchListButton.setOnClickListener() {
            viewModel.navigateWatchListFragment()
        }

        viewModel.navigateToWatchList.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToWatchListFragment())
                viewModel.navigateWatchListFragmentComplete()
            }
        })





        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return binding.root
    }


//    /**
//     * Inflates the overflow menu that contains filtering options.
//     */
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.search_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }


}
