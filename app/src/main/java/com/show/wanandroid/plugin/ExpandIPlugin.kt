package com.show.wanandroid.plugin

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.show.wanandroid.R
import com.showmethe.skinlib.plugin.IPlugin
import com.showmethe.speeddiallib.expand.ExpandMenuChildLayout

class ExpandIPlugin : IPlugin<ExpandMenuChildLayout> {

    override fun individuate(view: ExpandMenuChildLayout, attrName: String) {
        val color = when(attrName){
            "BlueTheme" -> ContextCompat.getColor(view.context, R.color.colorAccent)
            "RedTheme" -> ContextCompat.getColor(view.context, R.color.color_304ffe)
            else -> Color.WHITE
        }

        view.fabs.forEach {
            it.backgroundTintList = ColorStateList.valueOf(color)
        }
    }
}