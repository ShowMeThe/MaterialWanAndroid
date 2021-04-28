package com.show.wanandroid.plugin

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.showmethe.skinlib.plugin.IPlugin

class RefreshPlugin : IPlugin<SwipeRefreshLayout> {

    override fun individuate(view: SwipeRefreshLayout, attrName: String) {
        when (attrName) {
            themes_name[0] -> view.setColorSchemeResources(R.color.colorAccent)
            themes_name[1] -> view.setColorSchemeResources(R.color.color_304ffe)
            themes_name[2] -> view.setColorSchemeResources(R.color.color_6200ea)
        }
    }

    override fun individuate(
        view: SwipeRefreshLayout,
        attrName: String,
        colors: List<String>?
    ) {
        colors?.apply {
            if (colors.isNotEmpty()) {
                when(attrName){
                    themes_name[3],themes_name[4] ->view.setColorSchemeColors(Color.parseColor("#${colors[0]}"))
                }
            }
        }
    }
}