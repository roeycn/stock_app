package com.example.stockapp.stockinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.example.stockapp.R
import com.example.stockapp.stock.StockViewModel


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
        ViewModelProviders.of(this, StockInfoViewModel.Factory(activity.application))
            .get(StockInfoViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_info, container, false)
    }
}
