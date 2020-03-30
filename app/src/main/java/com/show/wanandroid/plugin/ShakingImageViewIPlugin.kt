package com.show.wanandroid.plugin

import androidx.core.content.ContextCompat
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.show.wanandroid.widget.ShakingImageView
import com.showmethe.skinlib.plugin.IPlugin

class ShakingImageViewIPlugin : IPlugin<ShakingImageView> {
    override fun individuate(view: ShakingImageView, attrName: String) {
        when (attrName) {
            themes_name[0] -> view.imageTintList =
                ContextCompat.getColorStateList(view.context, R.color.colorAccent)
            themes_name[1] -> view.imageTintList =
                ContextCompat.getColorStateList(view.context, R.color.color_ff4081)
            themes_name[2] -> view.imageTintList =
                ContextCompat.getColorStateList(view.context, R.color.color_7c4dff)
        }
    }
}