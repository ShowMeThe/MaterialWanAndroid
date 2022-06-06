package com.show.kcore.extras.status

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

fun ComponentActivity.statusBar(component: (Immerse.() -> Unit)) {
    component(Immerse.get())
}

fun Fragment.statusBar(component: (Immerse.() -> Unit)) {
    component(Immerse.get())
}

private var result = -1
fun Context.getStatusBarHeight(): Int {
    if (result == -1) {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
    }
    return result
}

class Immerse private constructor() {

    companion object {

        private val instant by lazy { Immerse() }

        fun get() = instant

    }

    /**
     * lightColor true systemBar text color is white else black
     */
    fun ComponentActivity.uiFullScreen(lightColor: Boolean = true) {
        updateStatusBarLayout(lightColor)
        updateContentViewFitSystem()
    }


    private fun ComponentActivity.updateStatusBarLayout(lightColor: Boolean = true) {
        if(Build.VERSION.SDK_INT >= 30){
            uiScreenUp30(lightColor)
        }else{
            uiScreenUnder30(lightColor)
        }

    }

    private fun ComponentActivity.updateContentViewFitSystem() {
        val contentView = findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        val childView = contentView.getChildAt(0)
        if (childView != null) {
            childView.fitsSystemWindows = false
        }
    }


    /**
     * Full Screen and hide StatusBar
     */
    fun ComponentActivity.windowFullScreen() {
        val up30 = Build.VERSION.SDK_INT >= 30
        if(up30){
            hideStatusBarUp30()
        }else{
            hideStatusBarUnder30()
        }
        fitCutout()
        lifecycle.addObserver(LifecycleEventObserver{ source,event ->
            if(event == Lifecycle.Event.ON_RESUME){
                if (up30) {
                    hideStatusBarUp30()
                } else {
                    hideStatusBarUnder30()
                }
            }
        })
    }

    private fun ComponentActivity.hideStatusBarUp30(){
        val controller = ViewCompat.getWindowInsetsController(window.decorView)
        controller?.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            hide(WindowInsetsCompat.Type.displayCutout())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        }
    }


    private fun ComponentActivity.hideStatusBarUnder30(){
        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = option
        decorView.fitsSystemWindows = false
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }


    private fun ComponentActivity.fitCutout() {
        val lp = window.attributes
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun ComponentActivity.uiScreenUp30(lightColor: Boolean){
        val controller = ViewCompat.getWindowInsetsController(window.decorView)
        controller?.apply {
            show(WindowInsetsCompat.Type.statusBars())
            isAppearanceLightStatusBars = lightColor.not()
        }
        window.statusBarColor = Color.TRANSPARENT
        window.setDecorFitsSystemWindows(false)
    }

    private fun ComponentActivity.uiScreenUnder30(lightColor: Boolean){
        val decorView = window.decorView

        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or if(lightColor){
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }else{
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        decorView.systemUiVisibility = option
        decorView.fitsSystemWindows = false
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }


}