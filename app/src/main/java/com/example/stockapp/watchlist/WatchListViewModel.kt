package com.example.stockapp.watchlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockapp.database.getDatabase
import com.example.stockapp.network.StockApi
import com.example.stockapp.network.StockApiService
import com.example.stockapp.network.StockApiServiceBuilder
import com.example.stockapp.network.StockSearchProperty
import com.example.stockapp.repository.StocksRepository
import kotlinx.coroutines.*
import java.util.ArrayList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


enum class StockApiStatus { LOADING, ERROR, DONE }

class WatchListViewModel(application: Application) : ViewModel() {

    private val _searchResponse = MutableLiveData<StockSearchProperty>()
    val searchResponse: LiveData<StockSearchProperty>
        get() = _searchResponse

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<StockApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<StockApiStatus>
        get() = _status


    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = SupervisorJob()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    // Create the database singleton.
    private val database = getDatabase(application)
    // Create the repository
    private val stocksRepository = StocksRepository(database)

    init {
        //getStockData()
        //   coroutineScope.launch {
        //     stocksRepository.refreshStocks()
    }

    /**
     * Factory for constructing StockViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WatchListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WatchListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


    //todo  -- allmost finished, now when user type char - request to search is sending good!!!
    //todo - just need to wait till the response is arriving and update _searchResponse - thinking about
    //todo wait till coroutineScope finishing

//     fun getSearchResults(keywords: String, apikey: String) {
//        coroutineScope.launch {
//           var searchResponse = StockApi.retrofitService.getSearchResults(keywords, apikey)
//           try {
//              // this will run on a thread managed by Retrofit
//              val result = searchResponse.await()
//              _searchResponse.value = result
//
//              Log.i("aaaaa","aaaaa   text entered: " + (_searchResponse.value))
//              Log.i("aaaaa", "aaaaa stocks size is: " + (_searchResponse.value?.metaData?.size))
//
//           } catch (e: Exception) {
//              _searchResponse.value = null
//           }
//        }
//     }

    fun getSearchResults(keywords: String, apikey: String) {
        //       var response = StockApi.retrofitService.getSearchResults(keywords, apikey)
//              try {
//                 // this will run on a thread managed by Retrofit
//                 val result = response.getCompleted()
//                 _searchResponse.value = result
//
//                 Log.i("aaaaa","aaaaa   text entered: " + (searchResponse.value))
//                 Log.i("aaaaa", "aaaaa stocks size is: " + (searchResponse.value?.metaData?.size))
//
//              } catch (e: Exception) {
//                 _searchResponse.value = null
//              }

        val request = StockApiServiceBuilder.buildService(StockApiService::class.java)
        val call = request.getSearchResults(keywords, apikey)

        call.enqueue(object : Callback<StockSearchProperty>{

            override fun onResponse(
                call: Call<StockSearchProperty>,
                response: Response<StockSearchProperty>
            ) {
                if (response.isSuccessful) {

//                    val stocksNameList: MutableList<String> = ArrayList()
//                    val str = response.body()?.metaData
//                    for (s in str.orEmpty()) {
//                       Log.i("metadata",s.name)
//                       println("aaaaaaaa printing the metaddata: " +s.name)
//                       stocksNameList.add(s.name)
//                    }
                    _searchResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<StockSearchProperty>, t: Throwable) {
                _searchResponse.value = null
            }


        }

        )


    }

    fun getStocksNameList(): MutableList<String> {
        val stocksNameList: MutableList<String> = ArrayList()
        val str = searchResponse.value?.metaData
        for (s in str.orEmpty()) {
            Log.i("metadata",s.name)
            println("aaaaaaaa printing the metaddata: " +s.name)
            stocksNameList.add(s.name)
        }
        return stocksNameList
    }

    var countries:MutableList<String> = ArrayList()
    var displayList:MutableList<String> = ArrayList()

    fun loadData(){
        countries.add("Afghanistan")
        countries.add("Albania")
        countries.add("Algeria")
        countries.add("Andorra")
        countries.add("Angola")
        countries.add("Antigua and Barbuda")
        countries.add("Argentina")
        countries.add("Armenia")
        countries.add("Australia")
        countries.add("Austria")
        countries.add("Azerbaijan")
        countries.add("Bahamas")
        countries.add("Bahrain")
        countries.add("Bangladesh")
        countries.add("Barbados")
        countries.add("Belarus")
        countries.add("Belgium")
        countries.add("Belize")
        countries.add("Benin")
        countries.add("Bhutan")
        countries.add("Bolivia")
        countries.add("Bosnia and Herzegovina")
        countries.add("Botswana")
        countries.add("Brazil")
        countries.add("Brunei")
        countries.add("Bulgaria")
        countries.add("Burkina Faso")
        countries.add("Burundi")
        countries.add("Cabo Verde")
        countries.add("Cambodia")
        countries.add("Cameroon")
        countries.add("Canada")
        countries.add("Central African Republic (CAR)")
        countries.add("Chad")
        countries.add("Chile")
        countries.add("China")
        countries.add("Colombia")
        countries.add("Comoros")
        countries.add("Democratic Republic of the Congo")
        countries.add("Republic of the Congo")
        countries.add("Costa Rica")
        countries.add("Cote d'Ivoire")
        countries.add("Croatia")
        countries.add("Cuba")
        countries.add("Cyprus")
        countries.add("Czech Republic")
        countries.add("Denmark")
        countries.add("Djibouti")
        countries.add("Dominica")
        countries.add("Dominican Republic")
        countries.add("Ecuador")
        countries.add("Egypt")
        countries.add("El Salvador")
        countries.add("Equatorial Guinea")
        countries.add("Eritrea")
        countries.add("Estonia")
        countries.add("Ethiopia")
        countries.add("Fiji")
        countries.add("Finland")
        countries.add("France")
        countries.add("Gabon")
        countries.add("Gambia")
        countries.add("Georgia")
        countries.add("Germany")
        countries.add("Ghana")
        countries.add("Greece")
        countries.add("Grenada")
        countries.add("Guatemala")
        countries.add("Guinea")
        countries.add("Guinea-Bissau")
        countries.add("Guyana")
        countries.add("Haiti")
        countries.add("Honduras")
        countries.add("Hungary")
        countries.add("Iceland")
        countries.add("India")
        countries.add("Indonesia")
        countries.add("Iran")
        countries.add("Iraq")
        countries.add("Ireland")
        countries.add("Israel")
        countries.add("Italy")
        countries.add("Jamaica")
        countries.add("Japan")
        countries.add("Jordan")
        countries.add("Kazakhstan")
        countries.add("Kenya")
        countries.add("Kiribati")
        countries.add("Kosovo")
        countries.add("Kuwait")
        countries.add("Kyrgyzstan")
        countries.add("Laos")
        countries.add("Latvia")
        countries.add("Lebanon")
        countries.add("Lesotho")
        countries.add("Liberia")
        countries.add("Libya")
        countries.add("Liechtenstein")
        countries.add("Lithuania")
        countries.add("Luxembourg")
        countries.add("Macedonia (FYROM)")
        countries.add("Madagascar")
        countries.add("Malawi")
        countries.add("Malaysia")
        countries.add("Maldives")
        countries.add("Mali")
        countries.add("Malta")
        countries.add("Marshall Islands")
        countries.add("Mauritania")
        countries.add("Mauritius")
        countries.add("Mexico")
        countries.add("Micronesia")
        countries.add("Moldova")
        countries.add("Monaco")
        countries.add("Mongolia")
        countries.add("Montenegro")
        countries.add("Morocco")
        countries.add("Mozambique")
        countries.add("Myanmar (Burma)")
        countries.add("Namibia")
        countries.add("Nauru")
        countries.add("Nepal")
        countries.add("Netherlands")
        countries.add("New Zealand")
        countries.add("Nicaragua")
        countries.add("Niger")
        countries.add("Nigeria")
        countries.add("North Korea")
        countries.add("Norway")
        countries.add("Oman")
        countries.add("Pakistan")
        countries.add("Palau")
        countries.add("Palestine")
        countries.add("Panama")
        countries.add("Papua New Guinea")
        countries.add("Paraguay")
        countries.add("Peru")
        countries.add("Philippines")
        countries.add("Poland")
        countries.add("Portugal")
        countries.add("Qatar")
        countries.add("Romania")
        countries.add("Russia")
        countries.add("Rwanda")
        countries.add("Saint Kitts and Nevis")
        countries.add("Saint Lucia")
        countries.add("Saint Vincent and the Grenadines")
        countries.add("Samoa")
        countries.add("San Marino")
        countries.add("Sao Tome and Principe")
        countries.add("Saudi Arabia")
        countries.add("Senegal")
        countries.add("Serbia")
        countries.add("Seychelles")
        countries.add("Sierra Leone")
        countries.add("Singapore")
        countries.add("Slovakia")
        countries.add("Slovenia")
        countries.add("Solomon Islands")
        countries.add("Somalia")
        countries.add("South Africa")
        countries.add("South Korea")
        countries.add("South Sudan")
        countries.add("Spain")
        countries.add("Sri Lanka")
        countries.add("Sudan")
        countries.add("Suriname")
        countries.add("Swaziland")
        countries.add("Sweden")
        countries.add("Switzerland")
        countries.add("Syria")
        countries.add("Taiwan")
        countries.add("Tajikistan")
        countries.add("Tanzania")
        countries.add("Thailand")
        countries.add("Timor-Leste")
        countries.add("Togo")
        countries.add("Tonga")
        countries.add("Trinidad and Tobago")
        countries.add("Tunisia")
        countries.add("Turkey")
        countries.add("Turkmenistan")
        countries.add("Tuvalu")
        countries.add("Uganda")
        countries.add("Ukraine")
        countries.add("United Arab Emirates (UAE)")
        countries.add("United Kingdom (UK)")
        countries.add("United States of America (USA)")
        countries.add("Uruguay")
        countries.add("Uzbekistan")
        countries.add("Vanuatu")
        countries.add("Vatican City (Holy See)")
        countries.add("Venezuela")
        countries.add("Vietnam")
        countries.add("Yemen")
        countries.add("Zambia")
        countries.add("Zimbabwe")
        displayList.addAll(countries)
    }


}
