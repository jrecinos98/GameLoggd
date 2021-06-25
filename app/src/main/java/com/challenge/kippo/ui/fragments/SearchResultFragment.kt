package com.challenge.kippo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.challenge.kippo.databinding.FragmentSearchFailedBinding
import com.challenge.kippo.databinding.FragmentSearchSuccessBinding
import com.challenge.kippo.ui.main.adapters.GameCardAdapter
import com.challenge.kippo.ui.main.GridItemDecoration
import com.challenge.kippo.ui.main.MGridLayoutManager
import com.challenge.kippo.util.Constants

/**
 * Child Fragment of Search Fragment
 * Displays a list of games if the result is successful
 * Displays an error fragment if the result is empty (No game matched search param)
 * If a gameCardAdapter is provided it will return The fragment with RecyclerView
 * @param gameCardAdapter Adapter to be used for the Recyclerview if successful
 */
//@AndroidEntryPoint
//TODO Split this up. It's more trouble than it is worth to combine the two
class SearchResultFragment(private var gameCardAdapter: GameCardAdapter? = null) : Fragment() {
    private lateinit var searchBinding: FragmentSearchSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        gameCardAdapter = GameCardAdapter(this)
        //retainInstance = true
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
    fun setAdapter(adapter: GameCardAdapter){
        this.gameCardAdapter = adapter
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