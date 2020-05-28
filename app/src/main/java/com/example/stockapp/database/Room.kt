package com.example.stockapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StockDao {
    @Query("select * from databasestock")
    fun getStockData(): LiveData<List<DatabaseStock>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStock(stock: DatabaseStock)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStocks(vararg stock: DatabaseStock)
}

// implement the VideosDatabase class
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