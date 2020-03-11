package com.example.qaapp.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.qaapp.R
import com.example.qaapp.database.Cars
import com.example.qaapp.databinding.FragmentOverviewBinding
import kotlinx.android.synthetic.main.list_item_car.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OverviewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
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


        // Inflate the layout for this fragment
        return binding.root
    }

}
