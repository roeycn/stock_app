//package com.example.stockapp.watchlist
//
//import android.R.attr
//import android.view.Menu
//import android.view.MenuItem
//import androidx.appcompat.view.ActionMode
//
//
//class ActionModeCallback : ActionMode.Callback {
//
//    private var multiSelect = false
//    private val selectedItems = ArrayList<Int>()
//
//    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
//        for (intItem in selectedItems) {
//            items.remove(intItem)
//        }
//        attr.mode.finish()
//        return true
//    }
//
//    // This method is called once when the ActionMode is first created and is where we should set up the Menu
//    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//        multiSelect = true;
//        menu!!.add("Delete");
//        return true;
//    }
//
//    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//       return false
//    }
//
//    // called whenever the user leaves the CAB or you call ‘finish()’ on the ActionMode.
//    override fun onDestroyActionMode(mode: ActionMode?) {
//        multiSelect = false;
//        selectedItems.clear();
//        notifyDataSetChanged();
//    }
//}
