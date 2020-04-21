package com.example.qaapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
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
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(stockBuilder.build())
    .build()



interface StockApiService {
    @GET("query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo")
    fun getStockData() :
            Deferred<StockDailyProperty>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object StockApi {
    val retrofitService : StockApiService by lazy { retrofit.create(StockApiService::class.java)}
}


//var client = OkHttpClient()
//
//var request: Request = Builder()
//    .url("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-summary?region=US&lang=en")
//    .get()
//    .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
//    .addHeader("x-rapidapi-key", "de48753aa6mshcbee840635b55f4p1123a8jsn0d72350cc07d")
//    .build()
//
//var response: Response = client.newCall(request).execute()