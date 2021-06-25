package com.challenge.kippo

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewpager.widget.ViewPager
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.backend.view_model.ViewModelFactory
import com.challenge.kippo.databinding.ActivityMainBinding
import com.challenge.kippo.ui.main.adapters.HomePagerAdapter
import com.challenge.kippo.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

//@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var homePagerAdapter: HomePagerAdapter
    private lateinit var viewModel : MainViewModel
    private lateinit var snackbar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(mainActivityBinding.root);
        //Creates a MainViewModel through its designated Factory
        viewModel = ViewModelProvider(this,
            ViewModelFactory(application)
        ).get()
        setUpUI()
        //Register broadcast receiver to be notified of network change events
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.let {
                it.registerDefaultNetworkCallback(networkCallback())
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if(!isNetworkAvailable(this)){
            showSnackBar(Constants.ERROR_MESSAGE.NO_NETWORK, "Ok")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * Sets up adapters and listeners to main activity views.
     */
    private fun setUpUI(){
        homePagerAdapter = HomePagerAdapter(this, supportFragmentManager)
        //Necessary to remove the tint that changes icon color.
        mainActivityBinding.bottomNavigationView.itemIconTintList = null
        mainActivityBinding.viewPager.adapter = homePagerAdapter
        //This way Favorite fragment and Trending fragment are not removed and reloaded constantly
        mainActivityBinding.viewPager.offscreenPageLimit = 2
        mainActivityBinding
            .bottomNavigationView
            .setOnNavigationItemSelectedListener(onNavigationItemSelected())
        mainActivityBinding
            .viewPager
            .addOnPageChangeListener(onPageChange())

    }
    /**
     * Returns an OnNavigationChangeListener which handles notifying the adapter to transition
     * from one fragment to another.
     */
    private fun onNavigationItemSelected(): BottomNavigationView.OnNavigationItemSelectedListener {
         return BottomNavigationView.OnNavigationItemSelectedListener { item ->
             //when keyword its a shorter way to use if, else-if, else blocks. In this case it works
             //as a switch statement.
             //Kotlin is smart enough to not need a return value
             when (item.itemId) {
                 R.id.trending -> {
                     mainActivityBinding.viewPager.setCurrentItem(
                         HomePagerAdapter.TRENDING_FRAG_INDEX,
                         true
                     )
                     true
                 }
                 R.id.search -> {
                     mainActivityBinding.viewPager.setCurrentItem(
                         HomePagerAdapter.SEARCH_FRAG_INDEX,
                         true
                     )
                     true
                 }
                 R.id.favorite -> {
                     mainActivityBinding.viewPager.setCurrentItem(
                         HomePagerAdapter.FAVORITE_FRAG_INDEX,
                         true
                     )
                     true
                 }
                 else -> false
            }
        }
    }

    /**
     * Handles scrolls from one fragment to the next within the ViewPager.
     */
    private fun onPageChange() : ViewPager.OnPageChangeListener {
        return object : ViewPager.OnPageChangeListener {
            /**
             * If the User scrolls rather than clicking on navigation icon we want to
             * mark the icon that corresponds to the scrolled page as checked so the icon
             * asset can be updated to the selected state.
             */
            override fun onPageSelected(position: Int) {
                when(position){
                    HomePagerAdapter.TRENDING_FRAG_INDEX -> {
                        mainActivityBinding
                            .bottomNavigationView
                            .menu
                            .findItem(R.id.trending).isChecked = true;
                    }
                    HomePagerAdapter.SEARCH_FRAG_INDEX -> {
                        mainActivityBinding
                            .bottomNavigationView
                            .menu
                            .findItem(R.id.search).isChecked = true;
                    }
                    HomePagerAdapter.FAVORITE_FRAG_INDEX -> {
                        mainActivityBinding
                            .bottomNavigationView
                            .menu
                            .findItem(R.id.favorite).isChecked = true;
                    }
                }
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        }
    }

    /**
     * Displays a snackbar with a message
     * @param message The message to be displayed
     * @param actionMessage The message of the action text to the right of the message
     */
    private fun showSnackBar(message: String, actionMessage : String){
        if(!::snackbar.isInitialized) {
            snackbar = Snackbar.make(mainActivityBinding.activityFrame,
                    message,
                    Snackbar.LENGTH_INDEFINITE)
            snackbar.apply {
                setAction(actionMessage){

                }
                setTextColor(getColor(R.color.white))
                setActionTextColor(getColor(R.color.teal))
                //anchorView = mainActivityBinding.bottomNavigationView
                view.layoutParams = (view.layoutParams as CoordinatorLayout.LayoutParams)
                        .apply {
                            anchorGravity = Gravity.TOP
                            gravity = Gravity.TOP
                            anchorId = mainActivityBinding.bottomNavigationView.id
                        }
            }
        }
        snackbar.show()
    }

    /**
     * Returns a callback method to be passed to the connection manager to be notified
     * of network changes.
     * @return callback to register.
     */
    private fun networkCallback() : ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            //When connection regained, fetch trending games for changes since last check
            //If snackbar is visible then dismiss it.
            override fun onAvailable(network: Network) {
                viewModel.getTrendingGames()
                if (::snackbar.isInitialized && snackbar.isShown) {
                    snackbar.dismiss()
                }

            }
            //Display snackbar with error message
            override fun onLost(network: Network) {
                showSnackBar(Constants.ERROR_MESSAGE.NO_NETWORK, "Ok")
            }
        }
    }

    /**
     * Checks network status to determine connectivity.
     */
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

}
