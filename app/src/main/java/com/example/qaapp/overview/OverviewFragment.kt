package com.example.qaapp.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.qaapp.R
import com.example.qaapp.database.Cars
import com.example.qaapp.databinding.FragmentOverviewBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // set to work with data binding
    //    val binding = FragmentOverviewBinding.inflate(inflater)

        val binding: FragmentOverviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_overview, container, false)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)


        val manager = GridLayoutManager(activity, 1)
        binding.carList.layoutManager = manager

        val adapter = OverviewAdapter()
        binding.carList.adapter = adapter

        val car1 = Cars("aaaa")
        val car2 = Cars("bbbb")
        val car3 = Cars("cccc")
        val car4 = Cars("dddd")
        val car5 = Cars("eeee")
        val car6 = Cars("ffff")
        val car7 = Cars("gggg")
        val car8 = Cars("hhhh")

        val carsListOfEight = mutableListOf<Cars>(car1,car2,car3,car4,car5,car6,car7,car8)

        viewModel.setCarCompany(carsListOfEight)

        viewModel.mCarCompany.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

}
