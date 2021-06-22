package com.challenge.kippo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.challenge.kippo.backend.storage.entities.GameData
import com.challenge.kippo.databinding.FragmentFavoriteBinding
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.ui.main.GameCardAdapter
import com.challenge.kippo.ui.main.MGridLayoutManager
import com.challenge.kippo.util.Constants
import com.challenge.kippo.util.Status

//TODO probably will combine Trending and Favorite as the only change is the source of what they observe
//Trending will observe trendingGames
//Favorite will observe liveData from db on Favorite games
class FavoriteFragment : Fragment(){
    private lateinit var mainViewModel: MainViewModel
    private lateinit var favoriteBinding: FragmentFavoriteBinding
    private lateinit var gameCardAdapter: GameCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java).apply {
            //setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        gameCardAdapter = GameCardAdapter(activity!!)
        gameCardAdapter.setOnFavoriteClick(mainViewModel::handleFavorite)
        retainInstance = true
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favoriteBinding = FragmentFavoriteBinding.inflate(inflater, container, false)
        favoriteBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = MGridLayoutManager(context, Constants.GRID_COL_COUNT)
            adapter = gameCardAdapter
        }
        observeGames()
        return favoriteBinding.root
    }

    private fun observeGames(){
        mainViewModel.getFavoriteGames().observe(this, { favoriteGames ->
            favoriteBinding.favoriteProgressbar.visibility = View.GONE
            gameCardAdapter.setGames(favoriteGames)
        })

    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
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