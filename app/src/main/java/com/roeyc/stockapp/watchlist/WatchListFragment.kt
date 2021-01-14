package com.roeyc.stockapp.watchlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dragdroprecyclerview.ItemMoveCallbackListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roeyc.stockapp.R
import com.roeyc.stockapp.databinding.FragmentWatchListBinding
import com.roeyc.stockapp.dialogs.APPDialog
import com.roeyc.stockapp.dialogs.AppDialogItem
import com.roeyc.stockapp.domain.UserStocksDataModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WatchListFragment : Fragment(), OnStartDragListener, APPDialog.APPDialogListener {

    private val TAG = WatchListFragment::class.java.simpleName

    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    lateinit var adapter: WatchListAdapter
    lateinit var touchHelper: ItemTouchHelper

    // Like in DBX
 //   lateinit var viewModelFactory: ViewModelProvider.Factory
 //   private lateinit var watchListViewModel: IBaseViewModel

    private val watchListViewModel: WatchListViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, WatchListViewModel.Factory(activity.application))
            .get(WatchListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

  //      watchListViewModel = ViewModelProviders.of(this, viewModelFactory).get(WatchListViewModel::class.java)

        Timber.d("onCreate")
        setupObservations()
        initRx()
    }

    private fun initRx() {

        var deepLinkingPageDisposable: Disposable? = null
        var brazeInternalUrlDisposable: Disposable? = null
        var deepLinkingPage = BehaviorSubject.create<String>()
        var brazeInternalUrl = BehaviorSubject.create<String>()

        val uriPubSub = BehaviorSubject.create<String>()
        val s = "https://driver.gett.ru/"
        Timber.i("gotoUri {}" + s)

        uriPubSub.onNext(s)

    //    context.apply { s.let { uriPubSub.onNext(it) } }

        if ( deepLinkingPageDisposable == null ){


        val newUri = uriPubSub
            .doOnNext { Timber.i("on new uriAction: " + it) }
            .share()


        val onNewDeepLinkingUri = newUri
           // .filter { uri -> DeepLinkingPage.values().firstOrNull { uri.contains(it.pageName) } != null }
            .doOnNext { Timber.i("onNewDeepLinkingUri") }
            .share()

        val onInternalUrl = newUri
             .doOnNext { Timber.i("onInternalUrl") }
             .share()



            val deepLinkingWhileAppRunning = onNewDeepLinkingUri
            .doOnNext { Timber.i("deepLinkingWhileAppRunning") }
            .share()

            val deepLinkingWhileAppIsStopped = onNewDeepLinkingUri
            .doOnNext { Timber.i("deepLinkingWhileAppIsStopped") }
            .share()

            val url = onInternalUrl
            .doOnNext { Timber.i("url") }
            .share()


        deepLinkingPageDisposable = Observable.merge(deepLinkingWhileAppRunning, deepLinkingWhileAppIsStopped)
            .doOnNext {
                Timber.i("page to linking to: " + it)
                deepLinkingPage.onNext(it)
            }.doOnError {  }
            .subscribe()


         brazeInternalUrlDisposable = newUri
             .doOnNext {
                 Timber.i("page to redirect: " + it)
                 brazeInternalUrl.onNext(it)
             }.doOnError {
             }.subscribe()

        }



    }


    private fun setupObservations() {
        //("setupObservations")
//        viewModel.showDialogParamsLiveData.observe({ lifecycle }) {
//            it?.let {
//
//            }
//        }

        watchListViewModel.apply {
            observeShowDialogLiveData(lifecycle) {
                showAppDialog(it)
            }
        }

    }

    private fun showAppDialog(appDialogItem: AppDialogItem, forceShow: Boolean = false) {
       // showPleaseLoginDialog()

        APPDialog.show(childFragmentManager, appDialogItem, forceShow)
    }

    override fun onCreateView(
        inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        var binding = FragmentWatchListBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = watchListViewModel
        adapter = WatchListAdapter(this, watchListViewModel)


        // in case i delete an item - then this called again and causing crash on bindviewholder -it getting an array out of index
        watchListViewModel.userStocks.observe(viewLifecycleOwner, Observer<List<UserStocksDataModel>> { userStocks ->
            userStocks?.apply {
                // adapter.setUserStocks(userStocks)
                // need to check how to handle without it
                adapter.submitList(userStocks)

                binding.fabAddStock.isEnabled = userStocks.size < 3
                binding.confirmSelection.isEnabled = userStocks.size > 2

                if (!binding.fabAddStock.isEnabled) {
                    binding.fabAddStock.visibility = View.INVISIBLE
                    binding.stockListTitle.text = String.format(
                        context!!.getString(R.string.stock_list_full_title))
                } else {
                    binding.fabAddStock.visibility = View.VISIBLE
                    binding.stockListTitle.text = String.format(
                        context!!.getString(R.string.stock_list_title), userStocks.size)
                }

                binding.totalValue.text = String.format(
                    context!!.getString(R.string.total_value), watchListViewModel.calculateTotalValue(userStocks).toString())
            }
        })

       // val userStocks = watchListViewModel.userStocks.value
       // adapter.setUserStocks(userStocks!!)
       // adapter.submitList(userStocks)
//        binding.totalValue.text = String.format(
//            context!!.getString(R.string.total_value), watchListViewModel.calculateTotalValue(userStocks).toString())

   //     adapter.setUserStocks(watchListViewModel.userStocks.value!!)

        val callback: ItemTouchHelper.Callback = ItemMoveCallbackListener(context!!, adapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.userStocksList)
        binding.userStocksList.adapter = adapter

        val manager = GridLayoutManager(activity, 1)
        binding.userStocksList.layoutManager = manager

        // possible to declare what to do in swipe right or left with:
//        binding.userStocksList.setOnTouchListener(object : OnSwipeTouchListener(){
//            override fun onSwipeLeft() {
//                Log.e("ViewSwipe", "Left")
//                super.onSwipeLeft()
//            }
//        });

        binding.confirmSelection.setOnClickListener() {
            watchListViewModel.sendYourPortfolioClicked()
        }

        // TODO: 12/16/20 - working with firbase Security Rules:
        //  only logged in user can vote , if its is first time or if he didnt in x min before.
        watchListViewModel.mSendYourPortfolio.observe(viewLifecycleOwner, Observer {
            it?.let {

                if (user != null) {
                    // User is signed in.
                } else {
                   // user is NOT signed in.
                    watchListViewModel.showPleaseLoginDialog()
                    return@Observer
                }


                MaterialAlertDialogBuilder(context)
                    .setTitle(R.string.send_your_portfolio_dailog_title)
                    .setMessage(R.string.send_your_portfolio_dailog_subtitle)
                    .setPositiveButton("YES") { dialog, which ->
                        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                        //the stock starting price - 1000$ is set when user add it to his list
                       // adapter.setUserStocks(viewModel.setStartingStocksPrice(adapter.mUserStocks))

                        binding.info.text = String.format(
                            context!!.getString(R.string.your_portfolio_saved_on), currentDate, currentTime)
                        binding.info.visibility = View.VISIBLE


                        var userLastRequest = db.collection("users").document(user!!.uid)
                        val stockA = db.collection("stocksSelection").document(adapter.currentList.get(0).symbol)
                        val stockB = db.collection("stocksSelection").document(adapter.currentList.get(1).symbol)
                        val stockC = db.collection("stocksSelection").document(adapter.currentList.get(2).symbol)

                        val timeStep = hashMapOf(
                            "lastRequest" to FieldValue.serverTimestamp()
                        )

                        val stockFields = hashMapOf(
                            "usersUids" to FieldValue.arrayUnion(user!!.uid),
                            "countUsers" to FieldValue.increment(1)
                        )

                        db.runBatch { batch ->
                            batch.set(
                                stockA,
                                stockFields,
                                SetOptions.merge()
                            )
                            batch.set(
                                stockB,
                                stockFields,
                                SetOptions.merge()
                            )
                            batch.set(
                                stockC,
                                stockFields,
                                SetOptions.merge()
                            )
                            batch.set(
                                userLastRequest,
                                timeStep
                            )
                        }.addOnSuccessListener {
                            Log.d(TAG, "Update DocumentSnapshot succeed")
                        }.addOnFailureListener{
                            Log.e(TAG, "Update DocumentSnapshot failed")
                            MaterialAlertDialogBuilder(context)
                                .setTitle("Info")
                                .setMessage("You need to wait 1 week for next vote")
                                .setNeutralButton("OK") { dialog, which ->
                                    dialog.dismiss()
                                }
                                .show()
                        }


                        // TODO: 12/13/20 check if its the correct approuch since we update 3
                        //  times and it might be to many requests
//                        for (item in adapter.currentList) {
//
//                            val selectedStock = db.collection("stocksSelection")
//                                .document(item.symbol.toString())
//
//                            selectedStock.update(
//                                "usersUids", FieldValue.arrayUnion(user!!.uid),
//                                "countUsers", FieldValue.increment(1)
//                            )
//
//                                .addOnSuccessListener { documentReference ->
//                                    Log.d(TAG, "Update DocumentSnapshot succeed")
//                                }
//                                .addOnFailureListener { e ->
//                                    val temp = ArrayList<kotlin.String>()
//                                    temp.add(user!!.uid)
//                                    val firstStock = FbStock(1, temp)
//
//                                    selectedStock.set(firstStock)
//                                    Log.d(
//                                        TAG,
//                                        "Update DocumentSnapshot failed, inserting new document",
//                                        e
//                                    )
//                                }
//                        }

                    }
                    .setNegativeButton("No") {dialog, which ->
                    }
                    .show()
            }
        })




        binding.fabAddStock.setOnClickListener() {
            watchListViewModel.navigateSearchFragment()
        }

        watchListViewModel.navigateToSearch.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(WatchListFragmentDirections.actionWatchListFragmentToSearchFragment())
                watchListViewModel.navigateSearchFragmentComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }


    companion object {
        var isMultiSelectOn = false
        val TAG = "MainActivity"
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        touchHelper.startDrag(viewHolder!!)
    }

    // dialog
    override fun onPositiveClicked(requestCode: Int?) {
        super.onPositiveClicked(requestCode)
    }
}

data class FbStock(val countUsers: Int, val usersUids: ArrayList<kotlin.String>)



