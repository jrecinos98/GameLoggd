package com.challenge.kippo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.challenge.kippo.databinding.FragmentTrendingBinding
import com.challenge.kippo.ui.main.GameCardAdapter
import com.challenge.kippo.ui.main.ListAdapter
import com.challenge.kippo.ui.main.MainViewModel

class TrendingFragment() :Fragment()  {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var trendingBinding: FragmentTrendingBinding
    private lateinit var gameCardAdapter: GameCardAdapter
    private lateinit var movieAdapter: ListAdapter
    val list = listOf("a", "b", "c", "d", "e", "f", "g" , "h")
    private val mNicolasCageMovies = listOf(
        MainFragment.Movie("Raising Arizona", 1987),
        MainFragment.Movie("Vampire's Kiss", 1988),
        MainFragment.Movie("Con Air", 1997),
        MainFragment.Movie("Gone in 60 Seconds", 1997),
        MainFragment.Movie("National Treasure", 2004),
        MainFragment.Movie("The Wicker Man", 2006),
        MainFragment.Movie("Ghost Rider", 2007),
        MainFragment.Movie("Knowing", 2009)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        gameCardAdapter = GameCardAdapter(list)
        movieAdapter = ListAdapter(mNicolasCageMovies)
        retainInstance = true
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        trendingBinding = FragmentTrendingBinding.inflate(inflater, container, false)
        trendingBinding.trendingRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager =  GridLayoutManager(context, GRID_COL_COUNT)
            adapter = gameCardAdapter
        }
        if(trendingBinding.trendingRecyclerView.adapter  != null){

            Log.d("TRENDING_FRAGMENT", "Value: " + trendingBinding.trendingRecyclerView.adapter.toString())
        }
        if(trendingBinding.trendingRecyclerView.adapter == null){
            Log.d("TRENDING_FRAGMENT", "Adapter null")
        }
        return trendingBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.d("TRENDING", "" +
                trendingBinding.trendingRecyclerView.isAttachedToWindow)
    }
    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        public const val GRID_COL_COUNT = 2

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): TrendingFragment {

            return TrendingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

}