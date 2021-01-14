package com.roeyc.stockapp.topstocks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roeyc.stockapp.watchlist.WatchListFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlin.collections.LinkedHashMap

class TopStocksViewModel : ViewModel() {

    private lateinit var searchEmitter: PublishSubject<Long>
    private var firestore = Firebase.firestore
    private lateinit var disposables: CompositeDisposable
    private val _topStocksData = MutableLiveData<LinkedHashMap<String, String>>()
    val topStocksData : LiveData<LinkedHashMap<String, String>>
        get() = _topStocksData

    init {
        initRx()
      //  initTopStocks()
        getTopStocks(3)
    }

    // TODO: 12/8/20 trating to call it with rx failed since getTopStocks() has been called first time and
    //  return an empty hashmap (the method used to retun the stocks) and after x time it was performing the
    //  addOnSuccessListener part (getting hashmap with stocks)
//    private fun initTopStocks() {
//        addSub(
//        Observable.just(getTopStocks(3))
//            .subscribeOn(Schedulers.newThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//               Log.d("TopStocksViewModel", "onNext")
//                Log.d("TopStocksViewModel", "top stocks recived from " +
//                        "cloud firestore: " + it.toString())
//                _topStocksData.value = it
//            }, {
//                Log.e("TopStocksViewModel", "onError")
//            }, {
//                Log.d("TopStocksViewModel", "onCompleted")
//            })
//        )
//    }


    private fun getTopStocks(limit: Long) {

        var stocksHM: LinkedHashMap<String, String> = LinkedHashMap()

        firestore.collection("stocksSelection").orderBy("countUsers", Query.Direction.DESCENDING).limit(limit)
            .get()
            .addOnSuccessListener { result ->
                Log.w(WatchListFragment.TAG, "Success getting documents.")

           //     binding.progressBar.visibility = View.GONE
           //     binding.mainContent.visibility = View.VISIBLE

                for (document in result) {
                    stocksHM.put(document.id, document.data.get("countUsers").toString())
                }
                _topStocksData.value = stocksHM

            }
            .addOnFailureListener { exception ->
                Log.w(WatchListFragment.TAG, "Error getting documents.", exception)
            }

    }


    private fun initRx() {
        disposables = CompositeDisposable()
    }

    @Synchronized
    private fun addSub(disposable: Disposable?) {
        if (disposable == null) return
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) disposables.dispose()
    }
}
