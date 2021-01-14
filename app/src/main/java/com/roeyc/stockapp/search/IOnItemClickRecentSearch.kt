package com.roeyc.stockapp.search

import com.roeyc.stockapp.domain.StockDataModel

interface IOnItemClickRecentSearch {
    fun onItemClicked(stock: StockDataModel)
}