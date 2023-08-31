package com.saxipapsi.saxi_movie.common

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class CommonRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val viewMap: MutableMap<Int, View> = HashMap()

    init {
        findViewItems(itemView)
    }

    private fun findViewItems(itemView: View) {
        addToMap(itemView)
        if (itemView is ViewGroup) {
            val childCount = itemView.childCount
            (0 until childCount)
                .map { itemView.getChildAt(it) }
                .forEach { findViewItems(it) }
        }
    }

    private fun addToMap(itemView: View) {
        viewMap[itemView.id] = itemView
    }
}