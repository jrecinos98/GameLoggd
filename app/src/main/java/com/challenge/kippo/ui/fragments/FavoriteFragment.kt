package com.challenge.kippo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.challenge.kippo.databinding.FragmentFavoriteBinding
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.ui.main.adapters.GameCardAdapter
import com.challenge.kippo.ui.main.GridItemDecoration
import com.challenge.kippo.ui.main.MGridLayoutManager
import com.challenge.kippo.util.Constants

/**
 * Fragment to display favorite games stored on local Room Database in a RecyclerView
 */
//@AndroidEntryPoint
class FavoriteFragment : Fragment(){

    private lateinit var mainViewModel: MainViewModel
    private lateinit var favoriteBinding: FragmentFavoriteBinding
    private lateinit var gameCardAdapter: GameCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        gameCardAdapter = GameCardAdapter(this)
        gameCardAdapter.setOnFavoriteClick(mainViewModel::handleFavorite)
       // retainInstance = true
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
            addItemDecoration(GridItemDecoration(Constants.GRID_ITEM_SPACING))
        }
        observeFavoriteGames()
        return favoriteBinding.root
    }

    /**
     * Observes LiveData object returned by viewModel to be notified of updates and changes
     * to the list of favorite games.
     * This allows the UI to automatically update
     */
    private fun observeFavoriteGames(){
        mainViewModel.getFavoriteGames().observe(viewLifecycleOwner, { favoriteGames ->
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