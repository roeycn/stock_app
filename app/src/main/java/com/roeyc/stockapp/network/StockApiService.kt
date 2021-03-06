package com.roeyc.stockapp.network

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
import retrofit2.http.Query


object StockApiServiceBuilder {


    private const val BASE_URL = "https://www.alphavantage.co/"
   // private val alphavantageFreeApiKey = "CITUC4CO27CTCF4D"


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

//    private val retrofit = Retrofit.Builder()
//        .addConverterFactory(MoshiConverterFactory.create(com.example.stockapp.network.moshi))
//        .addCallAdapterFactory(CoroutineCallAdapterFactory())
//        .baseUrl(com.example.stockapp.network.BASE_URL)
//        .client(stockBuilder.build())
//        .build()



    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    var stockLogging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var stockBuilder = OkHttpClient.Builder().addInterceptor(stockLogging)


    private val retrofit = Retrofit.Builder()
       // .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .client(stockBuilder.build())
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object StockApi {
    val retrofitService : StockApiService by lazy { StockApiServiceBuilder.buildService(StockApiService::class.java)}
}

interface StockApiService {
    // this API returns the latest price and volume information for a security of your choice.
    @GET("query?function=GLOBAL_QUOTE")
    fun getStockLastPrice(@Query("symbol") keywords: String,
                          @Query("apikey") apikey: String) :
            Deferred<StockLastPriceContainer>

    // This API returns intraday time series of the equity specified, covering extended trading hours where applicable
    @GET("query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo")
    fun getStockTimeSeriesIntraday() :
            Deferred<StockDailyProperty>

    //@GET("query?function=SYMBOL_SEARCH&keywords=Micro&apikey=demo")
    @GET("query?function=SYMBOL_SEARCH")
    fun getSearchResults(@Query("keywords") keywords: String,
                         @Query("apikey") apikey: String) :
            Call<StockSearchProperty>

}


