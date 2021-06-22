package com.challenge.kippo.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.challenge.kippo.R
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.databinding.FragmentSearchBinding
import com.challenge.kippo.ui.main.GameCardAdapter
import com.challenge.kippo.util.Status

class SearchFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchBinding: FragmentSearchBinding
    private lateinit var gameCardAdapter: GameCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java).apply {
            //setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        gameCardAdapter = GameCardAdapter(this)
        retainInstance = true
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        searchBinding.searchResultFragment
        searchBinding.textField.setOnEditorActionListener(onEditorActionListener())
        searchBinding.textField.imeOptions = EditorInfo.IME_ACTION_GO
        searchBinding.searchCancel.setOnClickListener(onCancelSearch())
        updateFragment(SearchResultFragment(gameCardAdapter),"success")
        observeSearchResults()
        return searchBinding.root
    }

    //Updates the current fragment shown on the Activity. The current fragment is added to the back of the stack.
    private fun updateFragment(frag: Fragment, tag: String) {
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        // Replace the contents of the container with the new fragment, add to stack and commit the transaction
        ft.replace(searchBinding.searchResultFragment.id, frag).addToBackStack(tag).commit()
    }
    private fun onCancelSearch() : View.OnClickListener{
        return object : View.OnClickListener{
            override fun onClick(v: View?) {
                //Reset text field
                searchBinding.textField.setText("")
            }

        }
    }

    private fun onEditorActionListener() : TextView.OnEditorActionListener{
        return object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when(actionId){
                    EditorInfo.IME_ACTION_GO -> {
                        mainViewModel.searchGame(v?.text.toString())
                        val inputManager: InputMethodManager = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.hideSoftInputFromWindow(activity!!.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                        return true
                    }
                }
                return false
            }

        }

    }

    /**
     * Observes LiveData object returned by viewModel to be notified of updates and changes
     * to the list of favorite games.
     * This allows the UI to automatically update
     */
    private fun observeSearchResults(){
        mainViewModel.getSearchObservable().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                Log.d("SEARCH_RESULT", resource.status.toString())
                when (resource.status) {
                    Status.SUCCESS -> {
                        searchBinding.searchProgressbar.visibility = View.GONE
                        resource.data?.let { list ->
                            if(list.isEmpty())
                                updateFragment(SearchResultFragment(), "fail")
                            else{
                                updateFragment(SearchResultFragment(gameCardAdapter),"success")
                                gameCardAdapter.setGames(list)
                            }
                        }
                    }
                    Status.LOADING -> {
                        searchBinding.searchProgressbar.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        searchBinding.searchProgressbar.visibility = View.GONE
                        Log.d("SEARCH_RESULT", resource.message.toString())
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