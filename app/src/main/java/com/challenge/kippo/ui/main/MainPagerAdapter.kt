package com.challenge.kippo.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val modified: Class<out Fragment>? = null

    private object TABS {
        val TITLES = arrayOf("Trending", "Search", "Favorites")
    }

    /**
     * Return the fragment at given position.
     * Only called when instantiated and when position returned by getItemPosition changes to POSITION_NONE
     * @param position The position of the item we want to get.
     * @return The fragment at that position
     */
    override fun getItem(position: Int): Fragment {
        return Fragment()
    }

    override fun getCount(): Int {
        return TABS.TITLES.size
    }

    /**
     * Sets the title for the Tabs.
     * @param position Position of fragment which corresponds to tab title
     * @return Title
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return TABS.TITLES[position]
    }

    /**
     * Returns POSITION_UNCHANGED when the input object is valid and should not be removed/updated.
     * @param object Object (Fragment) held by the adapter.
     * @return Integer.
     */
    override fun getItemPosition(`object`: Any): Int {
        //Check class and the super class to see if it matches the fragment that is to be removed
        //We check if it's assignable so we can check super class too.
        //Because the EmptyFragments each inherit from either ChatsFragment or CallsFragment and we need to check
        return if (modified!!.isAssignableFrom(`object`.javaClass)) {
            POSITION_NONE
        } else POSITION_UNCHANGED
    }

    /**
     * Updates the fragment which matches the given id.
     * @param id The id that maps to the fragment we are to modify
     */
    private fun updateFragment(id: String) {

        //Trigger a redraw of the viewPager.
        notifyDataSetChanged()
    }

    companion object {
        const val CHAT_FRAGMENT = "chat_list_fragment"
        const val CALLS_FRAGMENT = "call_list_fragment"
        const val CHAT_FRAG_INDEX = 0
        const val CALL_FRAG_INDEX = 1
    }
}