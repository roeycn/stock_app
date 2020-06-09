package com.example.stockapp.stock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.R
import com.example.stockapp.databinding.StockItemBinding
import com.example.stockapp.domain.StockDataModel

/**
 * RecyclerView Adapter for setting up data binding on the items in the list.
 */
class StockAdapter(val callback: StockFragment.StockClick) : RecyclerView.Adapter<StockViewHolder>() {

    /**
     * The stocks that our Adapter will show
     */
    var stocks: List<StockDataModel> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

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
            StockViewHolder.LAYOUT,
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
            //   it.stock = callback
        }
    }

    override fun getItemCount() = stocks.size

}

/**
 * ViewHolder for stock items. All work is done by data binding.
 */
class StockViewHolder(val viewDataBinding: StockItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.stock_item
    }
}