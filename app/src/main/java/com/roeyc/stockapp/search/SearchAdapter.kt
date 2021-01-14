package com.roeyc.stockapp.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes


class SearchAdapter(context: Context, @LayoutRes private val layoutResource: Int, private val allStocks: List<SearchResultDao>):
    ArrayAdapter<SearchResultDao>(context, layoutResource, allStocks), Filterable {

    private var mStocks: List<SearchResultDao> = allStocks

    override fun getCount(): Int {
       return mStocks.size
    }

    override fun getItem(index: Int): SearchResultDao? {
        return mStocks.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.hashCode().toLong()
    }

    // help in scrolling results issue
//    override fun getViewTypeCount(): Int {
//        return count
//    }

    override fun getItemViewType(index: Int): Int {
        return index;
    }


    private lateinit var view: View

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(layoutResource, parent, false)
        }

        val textViewSymbol: TextView = view.findViewById(android.R.id.text1)
        val textViewName: TextView = view.findViewById(android.R.id.text2)

        var searchResultDao: SearchResultDao? = getItem(position)

            if (searchResultDao != null) {
                searchResultDao = editStockName(searchResultDao)
                textViewSymbol.text = searchResultDao.stockSymbol
                textViewName.text = searchResultDao.stockName
        } else {
            textViewName.text = "Oops. There was a problem"
        }
        return view
    }

    fun <T> getSubList(list: List<T>, start: Int, end: Int): MutableList<T> {
        return list.subList(start, end + 1).toMutableList()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults( charSequence: CharSequence?, filterResults: Filter.FilterResults) {

                val queryString = charSequence?.toString()?.toLowerCase()

                if (filterResults != null) {
                    mStocks = filterResults.values as List<SearchResultDao>

                    // limit the results to 3 ... having scrolling results issue
                    if (mStocks.size > 3) {

                        val subList = getSubList(mStocks, 1, 3)

                        for (entry in mStocks) {
                            if (entry.stockSymbol.toLowerCase().equals(queryString)) {
                                subList.removeAt(0)
                                subList.add(0, entry)
                            }
                        }

                        mStocks = subList!!
                    }

                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()
                val filterResults = Filter.FilterResults()
                synchronized(filterResults) {
                filterResults.values = if (queryString == null || queryString.isEmpty())
                    allStocks
                else
                    allStocks.filter {
                        it.stockSymbol.toLowerCase().startsWith(queryString) ||
                                it.stockName.toLowerCase().startsWith(queryString)
                    }
                return filterResults
               }
            }
        }
    }

    fun editStockName(selectedStock: SearchResultDao?): SearchResultDao {
        if (selectedStock?.stockName?.length!! > 30) {
            selectedStock.stockName = selectedStock?.stockName?.substring(0, 30)
        }
        return selectedStock
    }

}