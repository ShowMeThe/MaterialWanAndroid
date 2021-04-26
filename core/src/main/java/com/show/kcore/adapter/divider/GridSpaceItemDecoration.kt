package com.show.kcore.adapter.divider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:59
 * Package Name:showmethe.github.core.adapter
 */
class GridSpaceItemDecoration(private val spanCount: Int, private val spacing: Int, private val orientation:Int = RecyclerView.VERTICAL, private val  includeEdge: Boolean = true) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % this.spanCount
        if (this.includeEdge) {
            when(orientation){
                RecyclerView.VERTICAL ->{
                    outRect.left = this.spacing - column * this.spacing / this.spanCount
                    outRect.right = (column + 1) * this.spacing / this.spanCount
                    if (position < this.spanCount) {
                        outRect.top = this.spacing
                    }
                    outRect.bottom = this.spacing
                }
                RecyclerView.HORIZONTAL ->{
                    outRect.top = this.spacing - column * this.spacing / this.spanCount
                    outRect.bottom = (column + 1) * this.spacing / this.spanCount
                    if (position < this.spanCount) {
                        outRect.left = this.spacing
                    }
                    outRect.right = this.spacing
                }
            }

        } else {
            when(orientation){
                RecyclerView.VERTICAL ->{
                    outRect.left = column * this.spacing / this.spanCount
                    outRect.right = this.spacing - (column + 1) * this.spacing / this.spanCount
                    if (position < this.spanCount) {
                        outRect.top = this.spacing
                    }
                    outRect.bottom = this.spacing
                }
                RecyclerView.HORIZONTAL ->{
                    outRect.top = column * this.spacing / this.spanCount
                    outRect.bottom = this.spacing - (column + 1) * this.spacing / this.spanCount
                    if (position < this.spanCount) {
                        outRect.left = this.spacing
                    }
                    outRect.right = this.spacing
                }
            }

        }

    }
}
