package com.show.wanandroid.plugin

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.show.wanandroid.widget.DrawingTextView
import com.showmethe.skinlib.plugin.IPlugin

class DrawingPlugin : IPlugin<DrawingTextView> {

    override fun individuate(view: DrawingTextView, attrName: String) {
        val color = when (attrName) {
            themes_name[0] -> ContextCompat.getColor(view.context, R.color.colorAccent)
            themes_name[1] -> ContextCompat.getColor(view.context, R.color.color_304ffe)
            themes_name[2] -> ContextCompat.getColor(view.context, R.color.color_6200ea)
            else -> Color.WHITE
        }
        view.setTextColor(color)
    }

    override fun individuate(view: DrawingTextView, attrName: String, colors: List<String>?) {
        colors?.apply {
            if (colors.isNotEmpty()) {
                when (attrName) {
                    themes_name[3],themes_name[4] -> {
                        view.setTextColor(Color.parseColor("#${colors[0]}"))
                    }
                }

            }
        }
    }
}