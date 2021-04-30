package com.show.wanandroid.widget

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import androidx.core.content.ContextCompat
import com.show.kcore.widget.SmartRelativeLayout
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.showmethe.skinlib.plugin.IPlugin

class SmartIPlugin : IPlugin<SmartRelativeLayout> {

    override fun individuate(view: SmartRelativeLayout, attrName: String) {
        view.attachToView(attachLoading = {
            progressbar.indeterminateTintMode = PorterDuff.Mode.SRC_IN
            when (attrName) {
                themes_name[0] -> progressbar.setIndicatorColor( ContextCompat.getColor(view.context, R.color.colorAccent))
                themes_name[1] -> progressbar.setIndicatorColor( ContextCompat.getColor(view.context, R.color.color_ff4081))
                themes_name[2] -> progressbar.setIndicatorColor(ContextCompat.getColor(view.context, R.color.color_6200ea))

            }
        })

    }

    override fun individuate(
        view: SmartRelativeLayout,
        attrName: String,
        colors: List<String>?
    ) {
        colors?.apply {
            if (colors.isNotEmpty()) {
                view.attachToView(attachLoading = {
                    when (attrName) {
                        themes_name[3], themes_name[4] -> progressbar.setIndicatorColor(Color.parseColor(
                            "#${colors[0]}"
                        ))

                    }
                })

            }
        }
    }
}