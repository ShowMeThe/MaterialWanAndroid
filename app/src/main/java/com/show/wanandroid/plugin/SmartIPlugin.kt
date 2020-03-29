package com.show.wanandroid.plugin

import android.util.Log
import com.show.wanandroid.R
import com.showmethe.skinlib.plugin.IPlugin
import showmethe.github.core.widget.common.SmartRelativeLayout

class SmartIPlugin : IPlugin<SmartRelativeLayout> {

    override fun individuate(view: SmartRelativeLayout, attrName: String) {
        when(attrName){
            "BlueTheme"->{
                view.setDefaultLoadingColorRes(R.color.colorAccent)
            }
            "RedTheme" ->{
                view.setDefaultLoadingColorRes(R.color.color_ff4081)
            }
        }
    }
}