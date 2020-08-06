package com.example.stockapp.stockinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.stockapp.databinding.FragmentStockInfoBinding
import com.example.stockapp.domain.StockDataModel


/**
 * A simple [Fragment] subclass.
 * Use the [StockInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StockInfoFragment : Fragment() {

    private val viewModel: StockInfoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val stockData = StockInfoFragmentArgs.fromBundle(arguments!!).selectedStock

        ViewModelProviders.of(this, StockInfoViewModel.Factory(stockData, activity.application))
            .get(StockInfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = FragmentStockInfoBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        viewModel.ss.observe(viewLifecycleOwner, Observer<StockDataModel> { stock ->
            stock?.apply {
                viewModel.setStockProperty(stock)
            }
        })

        setHasOptionsMenu(false)
        return binding.root
    }
}
