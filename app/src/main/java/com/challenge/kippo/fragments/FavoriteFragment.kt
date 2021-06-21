package com.challenge.kippo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.challenge.kippo.databinding.FragmentFavoriteBinding
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.ui.main.GameCardAdapter

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
        /*       val textView: TextView = root.findViewById(R.id.section_label)
               pageViewModel.text.observe(this, Observer<String> {
                   textView.text = it
               })
        */
        return favoriteBinding.root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

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