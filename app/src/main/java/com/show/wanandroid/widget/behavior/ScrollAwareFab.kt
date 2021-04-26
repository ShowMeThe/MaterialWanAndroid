package com.show.wanandroid.widget.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

open class ScrollAwareFab(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {


    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        if (dyConsumed > 0) {
            // User scrolled down and the FAB is currently visible -> hide the FAB

            onScrollDown(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        } else if (dyConsumed < 0 ) {
            // User scrolled up and the FAB is currently not visible -> show the FAB

            onScrollUp(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        }

    }


    open fun onScrollUp(coordinatorLayout: CoordinatorLayout,
                        child: FloatingActionButton,
                        target: View,
                        dxConsumed: Int,
                        dyConsumed: Int,
                        dxUnconsumed: Int,
                        dyUnconsumed: Int){

    }


    open fun onScrollDown(coordinatorLayout: CoordinatorLayout,
                          child: FloatingActionButton,
                          target: View,
                          dxConsumed: Int,
                          dyConsumed: Int,
                          dxUnconsumed: Int,
                          dyUnconsumed: Int){

    }

}