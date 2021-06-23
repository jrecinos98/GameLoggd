package com.challenge.kippo

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.challenge.kippo.backend.api.ClientManager
import com.challenge.kippo.databinding.ActivityMainBinding
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.backend.view_model.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.challenge.kippo.ui.main.HomePagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var homePagerAdapter: HomePagerAdapter
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(mainActivityBinding.root);
        val apiHelper = ClientManager(this)
        //Creates a MainViewModel through its designated Factory
        viewModel = ViewModelProvider(this, ViewModelFactory(this, apiHelper)).get()
        //TODO consider removing as the Interceptor can handle authentication when needed.
        //viewModel.authenticate()
        setUpUI()

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
            .setOnNavigationItemSelectedListener (onNavigationItemSelected())
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
                     mainActivityBinding.viewPager.setCurrentItem(HomePagerAdapter.TRENDING_FRAG_INDEX, true)
                    true
                 }
                 R.id.search -> {
                     mainActivityBinding.viewPager.setCurrentItem(HomePagerAdapter.SEARCH_FRAG_INDEX, true)
                     true
                 }
                 R.id.favorite -> {
                     mainActivityBinding.viewPager.setCurrentItem(HomePagerAdapter.FAVORITE_FRAG_INDEX, true)
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
                //If the User scrolls rather than clicking on navigation icon we want to
                //mark the icon that corresponds to the scrolled page as checked so the icon
                //asset can be updated to the selected state.
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
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
        }
    }
}
