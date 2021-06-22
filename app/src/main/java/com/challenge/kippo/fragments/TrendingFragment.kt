package com.challenge.kippo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.databinding.FragmentTrendingBinding
import com.challenge.kippo.ui.main.GameCardAdapter
import com.challenge.kippo.ui.main.MGridLayoutManager
import com.challenge.kippo.util.Constants
import com.challenge.kippo.util.Status


class TrendingFragment() :Fragment()  {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var trendingBinding: FragmentTrendingBinding
    private lateinit var gameCardAdapter: GameCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        gameCardAdapter = GameCardAdapter(activity!!)
        gameCardAdapter.setOnFavoriteClick(mainViewModel::handleFavorite)
        retainInstance = true
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        trendingBinding = FragmentTrendingBinding.inflate(inflater, container, false)
        //Set layoutManager and adapter for recyclerView
        trendingBinding.trendingRecyclerView.apply {
            setHasFixedSize(true)
//            layoutManager =  GridLayoutManager(context, GRID_COL_COUNT)
            layoutManager = MGridLayoutManager(context,Constants.GRID_COL_COUNT)
            adapter = gameCardAdapter
        }
        observeGames()


        return trendingBinding.root

    }



    private fun observeGames(){
        mainViewModel.getTrendingGames().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {

                        trendingBinding.trendingProgressbar.visibility = View.GONE
                        resource.data?.let { list ->
                            gameCardAdapter.setGames(list)
                        }
                    }
                    Status.LOADING -> {
                        trendingBinding.trendingProgressbar.visibility = View.VISIBLE
                    }
                    Status.ERROR ->{
                        trendingBinding.trendingProgressbar.visibility = View.GONE
                    }
                }

            }
        })

    }
    companion object {
        /**
         * Returns a new instance of this fragment.
         */
        @JvmStatic
        fun newInstance(): TrendingFragment {
            return TrendingFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }

}