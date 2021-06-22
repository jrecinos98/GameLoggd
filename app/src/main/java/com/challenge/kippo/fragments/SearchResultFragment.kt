package com.challenge.kippo.fragments

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.challenge.kippo.R
import com.challenge.kippo.databinding.FragmentSearchFailedBinding
import com.challenge.kippo.databinding.FragmentSearchSuccessBinding
import com.challenge.kippo.ui.main.GameCardAdapter
import com.challenge.kippo.ui.main.GridItemDecoration
import com.challenge.kippo.ui.main.MGridLayoutManager
import com.challenge.kippo.util.Constants

class SearchResultFragment(private val gameCardAdapter: GameCardAdapter? = null) : Fragment() {
    private lateinit var searchBinding: FragmentSearchSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        gameCardAdapter = GameCardAdapter(this)
        retainInstance = true
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        if(gameCardAdapter != null) {
            searchBinding = FragmentSearchSuccessBinding.inflate(inflater, container, false)
            searchBinding.recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = MGridLayoutManager(context, Constants.GRID_COL_COUNT)
                adapter = gameCardAdapter
                addItemDecoration(GridItemDecoration(Constants.GRID_ITEM_SPACING))
            }
            return searchBinding.root
        }else{
            return SearchFailedFragment().onCreateView(inflater,container,savedInstanceState)
        }
    }

    /**
     * Constructs a placeholder fragment for when the user has no conversations.
     */
    private class SearchFailedFragment : Fragment() {
        override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
                                   savedInstanceState: Bundle?): View {
            return FragmentSearchFailedBinding.inflate(inflater,container, false).root
        }
    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance( gameCardAdapter: GameCardAdapter?): SearchResultFragment {

            return SearchResultFragment(gameCardAdapter).apply {
                arguments = Bundle().apply {
                }
            }
        }
    }



}