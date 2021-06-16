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
        mainActivityBinding.viewPager.adapter = homePagerAdapter
        mainActivityBinding
                .bottomNavigationView
                .setOnNavigationItemSelectedListener {
                    item-> onNavigationItemSelected(item)
                }

        mainActivityBinding.viewPager.setOnScrollChangeListener()
        //Necessary to remove the tint that changed icon color.
        mainActivityBinding.bottomNavigationView.itemIconTintList = null


        //val tabs: TabLayout = findViewById(R.id.tabs)
        //tabs.setupWithViewPager(viewPager)
        //val fab: FloatingActionButton = findViewById(R.id.fab)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
    }
    private fun onNavigationItemSelected(item: MenuItem): Boolean {
         //when keyword its a shorter way to use if, else-if, else blocks. In this case it works
        //as a switch statement.
        //Kotlin is smart enough to not need a return value
        return when (item.itemId) {
            R.id.trending -> {
                Log.d("MAIN_ACTIVITY", "Clicked: "+ item.title)
                mainActivityBinding.viewPager.setCurrentItem(HomePagerAdapter.TRENDING_FRAG_INDEX, true)
                true
            }
            R.id.search -> {
                Log.d("MAIN_ACTIVITY", "Clicked: "+ item.title)

                mainActivityBinding.viewPager.setCurrentItem(HomePagerAdapter.SEARCH_FRAG_INDEX, true)
                true
            }
            R.id.favorite -> {
                Log.d("MAIN_ACTIVITY", "Clicked: "+ item.title)
                mainActivityBinding.viewPager.setCurrentItem(HomePagerAdapter.FAVORITE_FRAG_INDEX, true)
                true
            }
            else -> false
        }
    }
}