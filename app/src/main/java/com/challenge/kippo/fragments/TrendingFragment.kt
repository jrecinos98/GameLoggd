package com.challenge.kippo.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.challenge.kippo.backend.storage.entities.GameCard
import com.challenge.kippo.databinding.FragmentTrendingBinding
import com.challenge.kippo.ui.main.GameCardAdapter
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.util.Status
import java.util.ArrayList

class TrendingFragment() :Fragment()  {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var trendingBinding: FragmentTrendingBinding
    private lateinit var gameCardAdapter: GameCardAdapter

    //TODO remove
    private val list = arrayListOf<String>("a", "b", "c", "d", "e", "f", "g" , "h")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        gameCardAdapter = GameCardAdapter(list)
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
            layoutManager =  GridLayoutManager(context, GRID_COL_COUNT)
            adapter = gameCardAdapter
        }
        //observeGames()

        return trendingBinding.root

    }

    private fun observeGames(){
        mainViewModel.getGames().observe(this, Observer {
            it?.let { resource ->
                when (resource.status){
                    Status.SUCCESS -> {
                        resource.data?.let {
                                //list -> gameCardAdapter.setGames(list)
                        }
                    }
                }

            }
        })

    }
    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        const val GRID_COL_COUNT = 2

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