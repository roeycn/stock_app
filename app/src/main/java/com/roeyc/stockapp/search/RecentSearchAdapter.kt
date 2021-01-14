package com.roeyc.stockapp.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.roeyc.stockapp.R
import com.roeyc.stockapp.databinding.StockItemBinding
import com.roeyc.stockapp.domain.StockDataModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class RecentSearchAdapter() : RecyclerView.Adapter<RecentSearchAdapter.StockViewHolder>() {

    // a global variable which will emit String items
    private val clickSubject = PublishSubject.create<String>()

    override fun getItemCount() = stocks.size

    /**
     * The stocks that our Adapter will show
     */
    var stocks: List<StockDataModel> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val withDataBinding: StockItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.stock_item,
            parent,
            false
        )
        return StockViewHolder(withDataBinding)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     */
    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.stock = stocks[position]
            holder.bind(it.stock!!)
        }


        // https://medium.com/android-gate/recyclerview-item-click-listener-the-right-way-daecc838fbb9
        // from here we can have access to the the current clicked item
        // This will work perfectly but it is a bad practice because
        // It is not a good practice opening an Activity from a viewholder context
        // Note that onBindViewHolder is called as your views are populated during scrolling.
        // Thus you will have numerous calls to setOnClickListener.
        //holder.itemView.setOnClickListener{}
    }

    val clickEvent : Observable<String> = clickSubject

    /**
     * ViewHolder for stock items. All work is done by data binding.
     */
    inner class StockViewHolder(val viewDataBinding: StockItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        init {
            itemView.setOnClickListener {
                clickSubject.onNext(stocks[layoutPosition].toString())
            }
        }

        fun bind(stock: StockDataModel) {
            viewDataBinding.stockSymbol.text = stock.symbol
        }


}



}