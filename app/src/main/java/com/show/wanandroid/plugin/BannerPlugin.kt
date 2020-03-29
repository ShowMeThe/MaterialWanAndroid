package com.show.wanandroid.plugin

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.show.wanandroid.R
import com.showmethe.banner.Banner
import com.showmethe.skinlib.plugin.IPlugin

class BannerPlugin  : IPlugin<Banner> {
    override fun individuate(view: Banner, attrName: String) {
        val selector = when(attrName){
            "BlueTheme" -> ContextCompat.getColor(view.context, R.color.colorAccent)
            "RedTheme" -> ContextCompat.getColor(view.context, R.color.color_304ffe)
            else -> Color.WHITE
        }
        val unSelector = when(attrName){
            "BlueTheme" -> ContextCompat.getColor(view.context, R.color.color_5f4fc3f7)
            "RedTheme" -> ContextCompat.getColor(view.context, R.color.color_5fff4081)
            else -> Color.WHITE
        }
        view.setSelectorColor(selector)
        view.setUnSelectorColor(unSelector)
    }
}