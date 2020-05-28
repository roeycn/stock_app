package com.example.stockapp.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.database.Cars
import com.example.stockapp.databinding.ListItemCarBinding

class OverviewAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Cars, OverviewAdapter.ViewHolder>(OverviewDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
        holder.bind(item)
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Cars]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Cars]
     */
    class OnClickListener(val clickListener: (cars: Cars) -> Unit) {
        fun onClick(cars: Cars) = clickListener(cars)
    }


    class ViewHolder (val binding: ListItemCarBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cars: Cars) {
            binding.cars = cars
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCarBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class OverviewDiffCallback : DiffUtil.ItemCallback<Cars>() {

    override fun areItemsTheSame(oldItem: Cars, newItem: Cars): Boolean {
        return oldItem.company == newItem.company
    }

    override fun areContentsTheSame(oldItem: Cars, newItem: Cars): Boolean {
        return oldItem == newItem
    }

}