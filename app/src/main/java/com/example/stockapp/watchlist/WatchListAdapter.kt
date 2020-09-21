package com.example.stockapp.watchlist


import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dragdroprecyclerview.ItemMoveCallbackListener
import com.example.stockapp.databinding.UserStockItemBinding
import com.example.stockapp.domain.UserStocksDataModel
import okhttp3.internal.toImmutableList
import java.util.*
import java.util.Collections.unmodifiableList





class WatchListAdapter(private val startDragListener: OnStartDragListener, viewModel: WatchListViewModel):
    ListAdapter<UserStocksDataModel, WatchListAdapter.ViewHolder>(WatchListDiffCallback()), ItemMoveCallbackListener.Listener
    {
        val viewModel = viewModel;

//        override fun getCurrentList(): MutableList<UserStocksDataModel> {
//            return super.getCurrentList()
//        }

    // when we are using ListAdapter - it can handle our list and we dont need to use it locally
    //    https://www.youtube.com/watch?v=xPPMygGxiEo :

    //    var mUserStocks = emptyList<UserStocksDataModel>().toMutableList()

   //     fun setUserStocks(userStocks: List<UserStocksDataModel>) {
   //         mUserStocks.addAll(userStocks)
   //         notifyDataSetChanged()
   //     }

  //    override fun getItemCount(): Int {
  //        return mUserStocks.size
  //    }

    fun getStockAt(position: Int): UserStocksDataModel {
        return getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.binding.dragItem.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
            {
                this.startDragListener.onStartDrag(holder)
            }
            return@setOnTouchListener true
        }
    }

    class ViewHolder (val binding: UserStockItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(userStocksDataModel: UserStocksDataModel) {
            binding.stockSymbol.text = userStocksDataModel.symbol
            binding.stockName.text = userStocksDataModel.name
            binding.stockCost.text = userStocksDataModel.value.toString() + " $"
            // TODO("find a way to use string resource without context, option 1 - creating App class, option 2 - Resources ")
            //  val a = String.format(App.context.resources(R.string.stock_cost, userStocksDataModel.value.toString()))
            //  val b = Resources.getSystem().getString(R.string.stock_cost).toString()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserStockItemBinding.inflate(layoutInflater, parent, false)
                //   val list = ItemMoveCallbackListener.Listener
                return ViewHolder(binding)
            }
        }
    }





        override fun onRowMoved(fromPosition: Int, toPosition: Int) {
          //  var mUserStocks = emptyList<UserStocksDataModel>().toMutableList()
         //   var oldList = currentList

            val modifiableList: List<UserStocksDataModel> =  ArrayList(currentList)

            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(modifiableList, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(modifiableList, i, i - 1)
                }
            }

            submitList(modifiableList)
            onCurrentListChanged(currentList, modifiableList)
            //    notifyItemMoved(fromPosition, toPosition)
        }

        override fun onRowSelected(itemViewHolder: ViewHolder) {
            //do
        }

        override fun onRowClear(itemViewHolder: ViewHolder) {
            //do
        }

        override fun onRowSwiped(position: Int) {
            // need to remove it from db , i tried to pass the viewmodel to the adapter
            viewModel.removeStockFromUserStocks(getStockAt(position))
        //    mUserStocks.removeAt(position)
            notifyItemRemoved(position)
        //    notifyDataSetChanged()
        //    notifyItemRangeChanged(position, itemCount)

        }

        override fun onLongTap(index: Int) {
            //do
        }

        override fun onTap(index: Int) {
            //do
        }

    }


class WatchListDiffCallback : DiffUtil.ItemCallback<UserStocksDataModel>() {
    override fun areItemsTheSame(oldItem: UserStocksDataModel, newItem: UserStocksDataModel): Boolean {
        return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(oldItem: UserStocksDataModel, newItem: UserStocksDataModel): Boolean {
        return oldItem.symbol.equals(newItem.symbol) &&
                oldItem.name.equals(newItem.name) &&
                oldItem.value == newItem.value
    }
}


interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder?)

}