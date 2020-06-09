package com.example.stockapp


import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat


class SecondActivity : AppCompatActivity() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_second)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.search_menu, menu)
//        val activity: Activity = this
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchView: SearchView = MenuItemCompat
//            .getActionView(menu.findItem(R.id.action_search)) as SearchView
//        searchView.setSearchableInfo(
//            searchManager.getSearchableInfo(componentName)
//        )
//        searchView.setQueryHint("Search for users...")
//        val columNames =
//            arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
//     //   val viewIds = intArrayOf(R.id.text1)
//     //   val adapter: CursorAdapter = SimpleCursorAdapter(
////            this,
//     //       R.layout.simple_list_item_1, null, columNames, viewIds
//
//        )
//        searchView.setSuggestionsAdapter(adapter)
//     //   searchView.setOnSuggestionListener(getOnSuggestionClickListener())
//     //   searchView.setOnQueryTextListener(getOnQueryTextListener(activity, adapter))
//        return true
//    }
}
