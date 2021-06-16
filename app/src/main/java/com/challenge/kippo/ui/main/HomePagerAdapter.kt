package com.mchat.recinos.Activities.Home.Adapters

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.challenge.kippo.fragments.FavoriteFragment
import com.challenge.kippo.fragments.SearchFragment
import com.challenge.kippo.fragments.TrendingFragment

class HomePagerAdapter(private val context: Context, fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var modified: Class<out Fragment>? = null

    private object TABS {
         val TITLES = arrayOf("Trending", "Search", "Favorite")
    }
    /**
     * Return the fragment at given position.
     * Only called when instantiated and when position returned by getItemPosition changes to POSITION_NONE
     * @param position The position of the item we want to get.
     * @return The fragment at that position
     */
    override fun getItem(position: Int): Fragment {
        return when(position){
            TRENDING_FRAG_INDEX -> TrendingFragment()
            SEARCH_FRAG_INDEX -> SearchFragment()
            FAVORITE_FRAG_INDEX -> FavoriteFragment()
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return TABS.TITLES.size
    }

    /**
     * Sets the title for the Tabs.
     * @param position Position of fragment which corresponds to tab title
     * @return Title
     */
    override fun getPageTitle(position: Int): CharSequence ? {
        return TABS.TITLES[position]
    }

    /**
     * Returns POSITION_UNCHANGED when the input object is valid and should not be removed/updated.
     * @param object Object (Fragment) held by the adapter.
     * @return Integer.
     */
    override fun getItemPosition(`object`: Any): Int {
        return if (modified!!.isAssignableFrom(`object`.javaClass)) {
            PagerAdapter.POSITION_NONE
        } else PagerAdapter.POSITION_UNCHANGED
    }
    companion object {
        const val TRENDING_FRAGMENT = "trending_fragment"
        const val SEARCH_FRAGMENT = "search_fragment"
        const val FAVORITE_FRAGMENT = "favorite_fragment"
        const val TRENDING_FRAG_INDEX = 0
        const val SEARCH_FRAG_INDEX = 1
        const val FAVORITE_FRAG_INDEX = 2
    }

    init {
        //chatAdapter = ChatListAdapter()
        //callAdapter = CallListAdapter()
    }

    /**
     * Updates the id which matches the given id.
     * @param id The id that maps to the fragment we are to modify

    private fun updateFragment(id: String) {
        when (id) {
            CHAT_FRAGMENT ->                 //We want to modify the fragment in the ChatsFragment position (this could be an instance of EmptyChatsFragment)
                modified = ChatsFragment::class.java
            CALLS_FRAGMENT ->                 //We want to modify the fragment in the CallsFragment position (this could be an instance of EmptyCallsFragment)
                modified = CallsFragment::class.java
        }
        //Trigger a redraw of the viewPager.
        notifyDataSetChanged()
    }
    /**
     * Method called when the observer notifies of chat changes.
     * @param chats List of chat objects that the user has.
     */
    fun updateChats(chats: List<Chat?>) {
        chatAdapter.setChats(chats)
        //If the chat count increases from 0 or if the chat count goes to 0
        //Then the viewpager must update to display the chat list or empty chat frag, respectively .
        //Only update when this condition is met to prevent recreating the chat list with every change
        if (isChatAdapterEmpty() || chats.isEmpty()) {
            Log.d("CHATS_FRAGMENT", "Updated Adapter")
            updateFragment(CHAT_FRAGMENT)
        }
    }

    fun initChats(chats: List<Chat?>?) {
        chatAdapter.setChats(chats)
        updateFragment(CHAT_FRAGMENT)
    }

    fun updateCalls(calls: List<Call?>?) {}
    fun isChatAdapterEmpty(): Boolean {
        return chatAdapter.isEmpty()
    }

    fun isCallsEmpty(): Boolean {
        return callAdapter.isEmpty()
    }

    fun onChatClick(): LiveData<Chat> {
        return chatAdapter.onChatClick()
    } /*
    public LiveData<Chat> onCallClick(){
        return callAdapter.onCallClick();
    }*/
    */

}