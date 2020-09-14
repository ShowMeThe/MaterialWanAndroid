package showmethe.github.core.util.extras

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import showmethe.github.core.glide.TGlide
import showmethe.github.core.widget.common.AutoRecyclerView
import showmethe.github.core.widget.common.SmartRelativeLayout

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:55
 * Package Name:showmethe.github.core.util.extras
 */

inline fun <T : View> T.onGlobalLayout(crossinline  onLayout: T.()->Unit){
    if (viewTreeObserver.isAlive) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                onLayout()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}

inline fun <T : View> T.onPreDrawLayout(crossinline  onLayout: T.()->Unit){
    if (viewTreeObserver.isAlive) {
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {

            override fun onPreDraw(): Boolean {
                onLayout()
                viewTreeObserver.removeOnPreDrawListener(this)
                return false
            }
        })
    }
}



