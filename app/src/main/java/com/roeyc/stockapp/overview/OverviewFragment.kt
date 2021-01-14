package com.roeyc.stockapp.overview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.roeyc.stockapp.NavigationIconClickListener
import com.roeyc.stockapp.R
import com.roeyc.stockapp.databinding.FragmentOverviewBinding
import com.roeyc.stockapp.stocksList.Stocks
import java.text.SimpleDateFormat
import java.util.*

class OverviewFragment : Fragment() {

    private val viewModel: OverviewModel by lazy {
        ViewModelProviders.of(this).get(OverviewModel::class.java)
    }

    private lateinit var binding: FragmentOverviewBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var googleSignInClient: GoogleSignInClient
    val stocksInstance = Stocks.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {
        binding = FragmentOverviewBinding.inflate(inflater)

        //load stocks from file
        if (stocksInstance!!.stocks.isNullOrEmpty()) {
            stocksInstance.stocks = viewModel.readFileAsLinesUsingBufferedReader(context)
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this.context!!, gso)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.signInButton.setOnClickListener() {
            signIn()
        }

        binding.signOutButton.setOnClickListener() {
            signOut()
        }

        analytics = FirebaseAnalytics.getInstance(this.context!!)

        if (!isOnline()) {
            Toast.makeText(this.context,"please check your internet connection" , Toast.LENGTH_LONG).show()
        }

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(viewLifecycleOwner)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        binding.searchButton.setOnClickListener() {
            viewModel.navigateSearchFragment()

            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            analytics.logEvent("search_button_clicked") {
                param("click_time", currentTime)
            }
        }

        viewModel.navigateToSearch.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToSearchFragment())
                viewModel.navigateSearchFragmentComplete()
            }
        })

        binding.watchListButton.setOnClickListener() {
            viewModel.navigateWatchListFragment()
        }

        viewModel.navigateToWatchList.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToWatchListFragment())
                viewModel.navigateWatchListFragmentComplete()
            }
        })

        binding.topRanksButton.setOnClickListener() {
            viewModel.navigateToTopStocksFragment()
        }

        viewModel.navigateToTopStocks.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToTopStocksFragment())
                viewModel.navigateToTopStocksFragmentComplete()
            }
        })

        binding.marketBearOrBullButton.setOnClickListener() {
            viewModel.navigateToBearOrBullFragment()
        }

        viewModel.navigateToBearOrBull.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToBearOrBullFragment())
                viewModel.navigateToBearOrBullFragmentComplete()
            }
        })


        // Set up the toolbar.
        (activity as AppCompatActivity).setSupportActionBar(binding.appBar)
        binding.appBar.setNavigationOnClickListener(NavigationIconClickListener(activity!!, binding.grid, AccelerateDecelerateInterpolator(),
            ContextCompat.getDrawable(context!!, R.drawable.ic_menu_black), // Menu open icon
            ContextCompat.getDrawable(context!!, R.drawable.ic_close_black))) // Menu close icon

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return binding.root
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "firebaseAuth:" + user!!.uid)
                updateUI(user)
            }else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.w(TAG, "Sign in failed ")
                updateUI(null)
            }
        }
    }

    private fun signIn() {
        // reguler

        // val signIntent = googleSignInClient.signInIntent
        //   startActivityForResult(signIntent, RC_SIGN_IN)

        // with firebaseui
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
         //   AuthUI.IdpConfig.FacebookBuilder().build()
        )

        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.LoginTheme)
            .build(),
            1822
        )

    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this.context!!)
            .addOnCompleteListener {
                Toast.makeText(this.context,"signed out successfully" , Toast.LENGTH_LONG).show()
            }
        updateUI(null)
    }

    private fun delete() {
        AuthUI.getInstance()
            .delete(this.context!!)
            .addOnCompleteListener {
                Toast.makeText(this.context,"Delete the use from FirebaseAuth - successfully" , Toast.LENGTH_LONG).show()
            }
        updateUI(null)
    }

    private fun updateUI(user: FirebaseUser?) {
     //   hideProgressBar()
        if (user != null) {
            binding.signInText.text = getString(R.string.sign_in_signed_user, user.displayName)

            binding.signInButton.visibility = View.INVISIBLE
            binding.signOutButton.visibility = View.VISIBLE
        } else {
            binding.signInText.setText(R.string.sign_in)

            binding.signInButton.visibility = View.VISIBLE
            binding.signOutButton.visibility = View.INVISIBLE
        }
    }

    fun isOnline(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm!!.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    companion object {
        private const val TAG = "FireBase Auth"
        private const val RC_SIGN_IN = 1822
    }
}
