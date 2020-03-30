package com.show.wanandroid.plugin

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.showmethe.banner.Banner
import com.showmethe.skinlib.plugin.IPlugin

class BannerPlugin  : IPlugin<Banner> {


    override fun individuate(view: Banner, attrName: String) {
        val selector = when(attrName){
            themes_name[0] -> ContextCompat.getColor(view.context, R.color.colorAccent)
            themes_name[1] -> ContextCompat.getColor(view.context, R.color.color_304ffe)
            themes_name[2] -> ContextCompat.getColor(view.context, R.color.color_6200ea)
            else -> Color.WHITE
        }
        val unSelector = when(attrName){
            themes_name[0] -> ContextCompat.getColor(view.context, R.color.color_5f4fc3f7)
            themes_name[1]-> ContextCompat.getColor(view.context, R.color.color_5fff4081)
            themes_name[2] -> ContextCompat.getColor(view.context, R.color.color_5f7c4dff)
            else -> Color.WHITE
        }
        view.setSelectorColor(selector)
        view.setUnSelectorColor(unSelector)
    }
}