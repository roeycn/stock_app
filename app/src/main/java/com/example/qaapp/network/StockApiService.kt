package com.example.qaapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://www.alphavantage.co/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

var stockLogging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
var stockBuilder = OkHttpClient.Builder().addInterceptor(stockLogging)

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */

//object Network {
//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
//        .baseUrl(BASE_URL)
//        .client(stockBuilder.build())
//        .build()
//
//    val retrofitService = retrofit.create(StockApiService::class.java)
//}

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .client(stockBuilder.build())
        .build()

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object StockApi {
    val retrofitService : StockApiService by lazy { retrofit.create(StockApiService::class.java)}
}

interface StockApiService {
    @GET("query?function=GLOBAL_QUOTE&symbol=IBM&apikey=demo")
    fun getStockLastPrice() :
            Deferred<StockLastPriceContainer>

    @GET("query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo")
    fun getStockData() :
            Deferred<StockDailyProperty>

    //@GET("query?function=SYMBOL_SEARCH&keywords=Micro&apikey=demo")
    @GET("query?function=SYMBOL_SEARCH")
    fun getSearchResults(@Query("keywords") keywords: String,
                         @Query("apikey") apikey: String) :
            Deferred<StockSearchProperty>

}
