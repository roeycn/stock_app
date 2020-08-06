package com.example.stockapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

// In the DAO (data access object), you specify SQL queries and associate them with method calls
@Dao
interface StockDao {
    @Query("select * from databasestock")
    fun getAllStocksData(): LiveData<List<DatabaseStock>>

    @Query("select * from databasestock where symbol = :sym")
    fun getStockLiveData(sym: String): LiveData<DatabaseStock>

    @Query("select * from databasestock where symbol = :sym")
    fun getStockData(sym: String): LiveData<DatabaseStock>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStock(stock: DatabaseStock)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStocks(vararg stock: DatabaseStock)
}

// Room database class
// Room is a database layer on top of an SQLite database.
// Room uses the DAO to issue queries to its database.
// By default, to avoid poor UI performance, Room doesn't allow you to issue queries on the main thread.
// When Room queries return LiveData, the queries are automatically run asynchronously on a background thread.
@Database(entities = [DatabaseStock::class], version = 1)
abstract class StocksDatabase : RoomDatabase() {
    abstract val stockDao: StockDao
}

private lateinit var INSTANCE: StocksDatabase

fun getDatabase(context: Context): StocksDatabase {
    synchronized(StocksDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                StocksDatabase::class.java,
                "stocks").build()
        }
    }
    return INSTANCE
}