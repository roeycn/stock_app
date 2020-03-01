package com.example.qaapp.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qaapp.database.Cars
import com.example.qaapp.databinding.ListItemCarBinding

class OverviewAdapter() : ListAdapter<Cars, OverviewAdapter.ViewHolder>(OverviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = getItem(position)
        holder.bind(item)
    }


    class ViewHolder private constructor(val binding: ListItemCarBinding) :
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