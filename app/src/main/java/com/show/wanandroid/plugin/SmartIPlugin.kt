package com.show.wanandroid.plugin

import android.graphics.Color
import com.show.kcore.widget.SmartRelativeLayout
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.showmethe.skinlib.plugin.IPlugin

class SmartIPlugin : IPlugin<SmartRelativeLayout> {

    override fun individuate(view: SmartRelativeLayout, attrName: String) {
        when (attrName) {
            themes_name[0] -> view.setDefaultLoadingColorRes(R.color.colorAccent)
            themes_name[1] -> view.setDefaultLoadingColorRes(R.color.color_ff4081)
            themes_name[2] -> view.setDefaultLoadingColorRes(R.color.color_6200ea)
        }
    }


    override fun individuate(view: SmartRelativeLayout, attrName: String, colors: List<String>?) {
        colors?.apply {
            if(colors.size>0){
                when(attrName){
                    themes_name[3],themes_name[4]->view.setDefaultLoadingColor(Color.parseColor("#${colors[0]}"))
                }

            }
        }
    }
}