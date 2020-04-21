package com.example.qaapp.stock

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.qaapp.R
import com.example.qaapp.databinding.FragmentMarsBinding
import com.example.qaapp.databinding.FragmentStockBinding
import com.example.qaapp.mars.MarsViewModel
import com.example.qaapp.network.MarsApiFilter

class StockFragment : Fragment(){

        private val viewModel: StockViewModel by lazy {
            ViewModelProviders.of(this).get(StockViewModel::class.java)
        }

        override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?
        ): View? {

            //return inflater.inflate(com.example.qaapp.R.layout.fragment_mars, parent, false);

            var binding = FragmentStockBinding.inflate(inflater)

            binding.setLifecycleOwner(this)

            binding.viewModel = viewModel

         //   setHasOptionsMenu(true)

            return binding.root
        }

    }
