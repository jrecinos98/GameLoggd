package com.challenge.kippo.ui.main

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Custom GridLayoutManager object that scales the size of RecyclerView items depending on screen size
 */
class MGridLayoutManager(context: Context, colNum : Int) : GridLayoutManager(context,colNum) {

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        // force height of viewHolder here, this will override layout_height from xml
        lp.width = (width / 2.20).toInt()
        //Multiple height by the ratio of height and width to mimic design
        lp.height = (lp.width * 1.6060).toInt()
        //Add a margin in between recyclerview items
        lp.setMargins(0,0,0,0)
        return true
    }
}