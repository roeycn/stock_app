package com.roeyc.stockapp.topstocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.roeyc.stockapp.databinding.FragmentTopStocksBinding
import com.roeyc.stockapp.stocksList.Stocks

class TopStocksFragment : Fragment() {

    private lateinit var viewModel: TopStocksViewModel
    private lateinit var binding: FragmentTopStocksBinding
    val stocksInstance = Stocks.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider.NewInstanceFactory().create(TopStocksViewModel::class.java)
    }

    private fun initSubscriptions() {
        viewModel.topStocksData.observe(viewLifecycleOwner, Observer<LinkedHashMap<String, String>> {
           it?.let {
               update(it)
           }
    })
    }

    private fun update(topStocks: LinkedHashMap<String, String>) {
        if (!topStocks.isNullOrEmpty()) {
        //    logD("starting update the view")
            val keysItr = topStocks.keys.iterator()
            val valuesItr = topStocks.values.iterator()

            val stockSymbolA = keysItr.next()
            binding.firstStockSymbol.text = stockSymbolA
            binding.firstStockName.text = stocksInstance!!.stocks!!.get(stockSymbolA)
            binding.firstStockUsersCount.text = valuesItr.next() + " votes!"

            val stockSymbolB = keysItr.next()
            binding.secondStockSymbol.text = stockSymbolB
            binding.secondStockName.text = stocksInstance!!.stocks!!.get(stockSymbolB)
            binding.secondStockUsersCount.text = valuesItr.next() + " votes!"

            val stockSymbolC = keysItr.next()
            binding.thirdStockSymbol.text = stockSymbolC
            binding.thirdStockName.text = stocksInstance!!.stocks!!.get(stockSymbolC)
            binding.thirdStockUsersCount.text = valuesItr.next() + " votes!"
        }
        binding.progressBar.visibility = View.GONE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopStocksBinding.inflate(inflater)
        binding.progressBar.visibility = View.VISIBLE
        initSubscriptions()
        return binding.root
    }


}
