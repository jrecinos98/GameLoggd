package com.challenge.kippo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.databinding.FragmentTrendingBinding
import com.challenge.kippo.ui.main.GameCardAdapter
import com.challenge.kippo.ui.main.GridItemDecoration
import com.challenge.kippo.ui.main.MGridLayoutManager
import com.challenge.kippo.util.Constants
import com.challenge.kippo.util.Status

/**
 * Fragment to display trending/popular games based on IGDB fields
 * Game data is fetched with Retrofit (and OkHttp) and displayed in RecyclerView
 */
class TrendingFragment() :Fragment()  {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var trendingBinding: FragmentTrendingBinding
    private lateinit var gameCardAdapter: GameCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        gameCardAdapter = GameCardAdapter(this)
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
            layoutManager = MGridLayoutManager(context,Constants.GRID_COL_COUNT)
            adapter = gameCardAdapter
            addItemDecoration(GridItemDecoration(Constants.GRID_ITEM_SPACING))
        }
        observeTrendingGames()
        return trendingBinding.root

    }

    /**
     * Observes LiveData object returned by viewModel to be notified of updates and changes
     * to the list of trending games
     */
    private fun observeTrendingGames(){
        mainViewModel.getTrendingGames().observe(viewLifecycleOwner, Observer {
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