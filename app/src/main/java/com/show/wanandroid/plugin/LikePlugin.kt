package com.show.wanandroid.plugin

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.show.wanandroid.widget.LikeView
import com.showmethe.skinlib.plugin.IPlugin

class LikePlugin : IPlugin<LikeView> {
    override fun individuate(view: LikeView, attrName: String) {
        var color = 0
        when (attrName) {
            themes_name[0] -> {
                color = ContextCompat.getColor(view.context,R.color.colorAccent)
            }
            themes_name[1] -> {
                color = ContextCompat.getColor(view.context,R.color.color_304ffe)
            }
            themes_name[2] -> {
                color = ContextCompat.getColor(view.context,R.color.color_6200ea)

            }
        }
        view.dotColor(color)
        view.likeColor(color)
    }

    override fun individuate(view: LikeView, attrName: String, colors: List<String>?) {
        colors?.apply {
            if (colors.isNotEmpty()) {
                when(attrName){
                    themes_name[3], themes_name[4] ->{
                        val color = Color.parseColor("#${colors[0]}")
                        view.dotColor(color)
                        view.likeColor(color)
                    }
                }
            }
        }
    }
}