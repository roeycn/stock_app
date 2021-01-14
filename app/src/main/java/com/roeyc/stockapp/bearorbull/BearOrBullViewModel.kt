package com.roeyc.stockapp.bearorbull

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roeyc.stockapp.BaseViewModel
import com.roeyc.stockapp.watchlist.WatchListFragment
import com.roeyc.stockapp.watchlist.WatchListViewModel
import timber.log.Timber

class BearOrBullViewModel(application: Application) : BaseViewModel(application) {

    private val TAG = BearOrBullViewModel::class.java.simpleName

    private var firestore = Firebase.firestore
    private var auth: FirebaseAuth
    private val user : FirebaseUser

    private var _totalUsersVotes = MutableLiveData<HashMap<String, String>>()
    val totalUsersVotes: LiveData<HashMap<String, String>>
        get() = _totalUsersVotes

    private var _bullButtonPressed = MutableLiveData<Boolean>()
    val bullButtonPressed: LiveData<Boolean>
        get() = _bullButtonPressed

    private var _bearButtonPressed = MutableLiveData<Boolean>()
    val bearButtonPressed: LiveData<Boolean>
        get() = _bearButtonPressed


    init {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!

    //    getVotes()
    }

    fun buttonPressed(bull: Boolean){
        if (bull) {
            _bullButtonPressed.value = true
        } else {
            _bearButtonPressed.value = true
        }
    }

    fun onButtonPressedComplete() {
        _bullButtonPressed.value = null
        _bearButtonPressed.value = null
    }


    fun setUserVote(bull: Boolean) {

        val userVote : DocumentReference

        if (bull) {
            userVote = firestore.collection("bearOrBull").document("Bull")
        } else {
            userVote = firestore.collection("bearOrBull").document("Bear")
        }

        val voteFields = hashMapOf(
            "userIds" to FieldValue.arrayUnion(user!!.uid),
            "countUsers" to FieldValue.increment(1)
        )

        firestore.runBatch { batch->
            batch.set(
                userVote,
                voteFields
            )

        }.addOnSuccessListener {
            Timber.d("update succeed")
        }.addOnFailureListener {
            Timber.d("update failed: {}" + it)
        }

    }

    fun getVotes() {

        var response: LinkedHashMap<String, String> = LinkedHashMap()

     //   need to check if can use: firestore.collection("bearOrBull").whereIn("countUsers", listOf("Bull", "Bear"))
          firestore.collection("bearOrBull").orderBy("countUsers")
            .get()
            .addOnSuccessListener { result ->
                Log.d(WatchListFragment.TAG, "Success getting documents.")

                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    response.put(document.id, document.data.get("countUsers").toString())
                }
                _totalUsersVotes.value = response
            }
            .addOnFailureListener { exception ->
                Log.d(WatchListFragment.TAG, "Error getting documents.", exception)
            }
    }

    /**
     * Factory for constructing StockViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BearOrBullViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BearOrBullViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}