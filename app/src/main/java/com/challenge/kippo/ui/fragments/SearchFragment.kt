package com.challenge.kippo.ui.fragments

import android.R.drawable
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.challenge.kippo.R
import com.challenge.kippo.backend.view_model.MainViewModel
import com.challenge.kippo.databinding.FragmentSearchBinding
import com.challenge.kippo.ui.main.adapters.GameCardAdapter
import com.challenge.kippo.util.Result


//@AndroidEntryPoint
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
        gameCardAdapter.setOnFavoriteClick(mainViewModel::handleFavorite)
        //retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false)
        //Sets listener to the keyboard editor action
        searchBinding.textField.apply{
            setOnEditorActionListener(onEditorActionListener())
            //Sets the keyboard editor action to the GO state (so we can search)
            searchBinding.textField.imeOptions = EditorInfo.IME_ACTION_GO
        }
        searchBinding.searchCancel.setOnClickListener(onCancelSearch())
        updateFragment(SUCCESS_FRAG_TAG)
        observeSearchResults()
        return searchBinding.root
    }

    /**
     * @return listener for the cancel icon on the toolbar.
     */
    private fun onCancelSearch() : View.OnClickListener{
        return View.OnClickListener {
            //Reset text field to empty string
            searchBinding.textField.setText("")
        }
    }

    /**
     * Listens to the Keyboard Action presses
     */
    private fun onEditorActionListener() : TextView.OnEditorActionListener{
        return object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when(actionId){
                    EditorInfo.IME_ACTION_GO -> {
                        mainViewModel.searchGame(v?.text.toString())
                        return true
                    }
                }
                return false
            }

        }

    }

    /**
     * Updates the current fragment shown on the child fragment
     * The current fragment is added to the back of the stack.
     * @param tag The tag of the fragment that will be loaded
     */
    private fun updateFragment(tag: String) {
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        //TODO Not sure if recreating the fragment every time is a good idea.
        val newFrag : Fragment = if(tag == SUCCESS_FRAG_TAG){
            SearchResultFragment(gameCardAdapter)
        } else{
            SearchResultFragment()
        }
        // Replace the contents of the container with the new fragment, add to stack and commit the transaction
        ft.replace(searchBinding.searchResultFragment.id, newFrag, tag).addToBackStack(null).commit()
    }
    //FIXME When configuration changes occur the SearchResultFragment is recreated and the adapter is null
    // Therefore it will always display the SearchFail fragment and never updates if we use the ones stored
    // by the Fragment manager
    private fun findFragment(tag : String) : Fragment{
        val fragment = childFragmentManager.findFragmentByTag(tag)
            ?: if(tag == SUCCESS_FRAG_TAG){
                SearchResultFragment(gameCardAdapter)
            } else{
                SearchResultFragment()
            }
        (fragment as SearchResultFragment).setAdapter(gameCardAdapter)
        return fragment
    }

    /**
     * Observes LiveData object returned by viewModel to be notified of updates and changes
     * to the list of favorite games.
     * This allows the UI to automatically update
     */
    private fun observeSearchResults(){
        mainViewModel.getSearchObservable().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Result.Status.SUCCESS -> {
                        searchBinding.searchProgressbar.visibility = View.GONE
                        if(resource.status == Result.Status.SUCCESS) {
                            resource.data?.let { list ->
                                if (list.isEmpty()) {
                                    updateFragment(FAILED_FRAG_TAG)
                                } else {
                                    val inputManager: InputMethodManager =
                                        requireContext().getSystemService(
                                            Context.INPUT_METHOD_SERVICE
                                        ) as InputMethodManager
                                    inputManager.hideSoftInputFromWindow(
                                        requireActivity().currentFocus?.windowToken,
                                        InputMethodManager.HIDE_NOT_ALWAYS
                                    )
                                    gameCardAdapter.setGames(list)
                                    updateFragment(SUCCESS_FRAG_TAG)
                                }
                            }
                        }
                    }
                    Result.Status.LOADING -> {
                        searchBinding.searchProgressbar.visibility = View.VISIBLE
                    }
                    Result.Status.ERROR -> {
                        searchBinding.searchProgressbar.visibility = View.GONE
                        Log.d("ERROR_MESSAGE", resource.message.toString())
                    }
                }

            }
        })

    }

    companion object {
        const val SUCCESS_FRAG_TAG = "success"
        const val FAILED_FRAG_TAG = "fail"
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): SearchFragment {

            return SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}