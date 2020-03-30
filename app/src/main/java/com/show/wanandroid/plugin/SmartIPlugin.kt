package com.show.wanandroid.plugin

import android.util.Log
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.showmethe.skinlib.plugin.IPlugin
import showmethe.github.core.widget.common.SmartRelativeLayout

class SmartIPlugin : IPlugin<SmartRelativeLayout> {

    override fun individuate(view: SmartRelativeLayout, attrName: String) {
        when (attrName) {
            themes_name[0] -> view.setDefaultLoadingColorRes(R.color.colorAccent)
            themes_name[1] -> view.setDefaultLoadingColorRes(R.color.color_ff4081)
            themes_name[2] -> view.setDefaultLoadingColorRes(R.color.color_6200ea)
        }
    }
}