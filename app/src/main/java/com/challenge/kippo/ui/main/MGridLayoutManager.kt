package com.challenge.kippo.ui.main

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Custom GridLayoutManager object that scales the size of RecyclerView items depending on screen size
 *
 */
class MGridLayoutManager(context: Context, colNum : Int) : GridLayoutManager(context,colNum) {

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        // force height of viewHolder here, this will override layout_height from xml
        lp.width = (width / 2.20).toInt()
        lp.height = (lp.width * 1.6060).toInt()
        lp.setMargins(20,0,0,30)
        return true
    }
}