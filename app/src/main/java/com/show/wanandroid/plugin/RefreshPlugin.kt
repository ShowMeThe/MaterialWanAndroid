package com.show.wanandroid.plugin

import android.util.Log
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.show.wanandroid.R
import com.showmethe.skinlib.plugin.IPlugin

class RefreshPlugin : IPlugin<SwipeRefreshLayout> {

    override fun individuate(view: SwipeRefreshLayout, attrName: String) {
        when(attrName){
            "BlueTheme"->{
                view.setColorSchemeResources(R.color.colorAccent)
            }
            "RedTheme" ->{
                view.setColorSchemeResources(R.color.color_ff4081)
            }
        }
    }
}