package com.roeyc.stockapp.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roeyc.stockapp.R
import kotlinx.android.synthetic.main.country_child.view.*

class CountryAdapter(items : List<String>,ctx: Context) : RecyclerView.Adapter<CountryAdapter.ViewHolder>(){

    private var list = items
    private var context = ctx

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val name = v.country_name!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.name?.text = list[position]

//        var item = getItem(position)
//        holder.itemView.setOnClickListener {
//            onClickListener.onClick(item)
//        }
//        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.country_child,parent,false))
    }
}