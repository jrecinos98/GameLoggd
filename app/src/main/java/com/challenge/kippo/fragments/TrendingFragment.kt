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
import com.challenge.kippo.util.Status


class TrendingFragment() :Fragment()  {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var trendingBinding: FragmentTrendingBinding
    private lateinit var gameCardAdapter: GameCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        gameCardAdapter = GameCardAdapter(activity!!)
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
            layoutManager = object : GridLayoutManager(context, GRID_COL_COUNT) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    // force height of viewHolder here, this will override layout_height from xml
                    lp.width = (width / 2.20).toInt()
                    lp.height = (lp.width * 1.6060).toInt()
                    lp.setMargins(0,0,0,10)
                    return true
                }
            }
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
                        resource.data?.let { list ->
                            gameCardAdapter.setGames(list)
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