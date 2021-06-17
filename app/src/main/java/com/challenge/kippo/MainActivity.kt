package com.challenge.kippo

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.challenge.kippo.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mchat.recinos.Activities.Home.Adapters.HomePagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var homePagerAdapter: HomePagerAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(mainActivityBinding.root);

        homePagerAdapter = HomePagerAdapter(this, supportFragmentManager)
        //Necessary to remove the tint that changes icon color.
        mainActivityBinding.bottomNavigationView.itemIconTintList = null

        mainActivityBinding.viewPager.adapter = homePagerAdapter
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
                     Log.d("MAIN_ACTIVITY", "Clicked: " + item.title)
                     mainActivityBinding.viewPager.setCurrentItem(HomePagerAdapter.TRENDING_FRAG_INDEX, true)
                    true
                 }
                 R.id.search -> {
                     Log.d("MAIN_ACTIVITY", "Clicked: " + item.title)
                     mainActivityBinding.viewPager.setCurrentItem(HomePagerAdapter.SEARCH_FRAG_INDEX, true)
                     true
                 }
                 R.id.favorite -> {
                     Log.d("MAIN_ACTIVITY", "Clicked: " + item.title)
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
