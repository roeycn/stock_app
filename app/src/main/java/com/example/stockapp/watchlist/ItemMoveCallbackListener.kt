package com.dragdroprecyclerview

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.stockapp.App.Companion.context
import com.example.stockapp.R
import com.example.stockapp.watchlist.WatchListAdapter
import com.google.android.material.color.MaterialColors.ALPHA_FULL


class ItemMoveCallbackListener(val context: Context, val adapter: WatchListAdapter) : ItemTouchHelper.Callback() {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_item)
    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
    private val intrinsicHeight = deleteIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }


    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.itemViewType != target.itemViewType) {
            return false
        }
        // Notify the adapter of the move
        adapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is WatchListAdapter.ViewHolder) {
                adapter.onRowSelected(viewHolder)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is WatchListAdapter.ViewHolder) {
            adapter.onRowClear(viewHolder)
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onRowSwiped(viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int,
        isCurrentlyActive: Boolean) {
        if (actionState === ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            val alpha =
                ALPHA_FULL - Math.abs(dX) / viewHolder.itemView.width.toFloat()
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX

            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top
            val isCanceled = dX == 0f && !isCurrentlyActive

            if (isCanceled) {
                clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                return
            }

            // Draw the red delete background
            background.color = backgroundColor
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight

            // Draw the delete icon
            deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    interface Listener {
        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(itemViewHolder: WatchListAdapter.ViewHolder)
        fun onRowClear(itemViewHolder: WatchListAdapter.ViewHolder)
        fun onRowSwiped(position: Int)
        fun onLongTap(index : Int)
        fun onTap(index : Int)
    }
}