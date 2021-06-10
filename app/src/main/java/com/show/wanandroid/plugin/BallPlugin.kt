package com.show.wanandroid.plugin

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.show.wanandroid.widget.BallRotationProgressBar
import com.showmethe.skinlib.plugin.IPlugin

class BallPlugin : IPlugin<BallRotationProgressBar> {
    override fun individuate(view: BallRotationProgressBar, attrName: String) {
        val color = when (attrName) {
            themes_name[0] -> ContextCompat.getColor(view.context, R.color.colorAccent)
            themes_name[1] -> ContextCompat.getColor(view.context, R.color.color_304ffe)
            themes_name[2] -> ContextCompat.getColor(view.context, R.color.color_6200ea)
            else -> ContextCompat.getColor(view.context, R.color.colorAccent)
        }
        view.setFirstBallColor(color)
        view.setSecondBallColor(color)
    }

    override fun individuate(
        view: BallRotationProgressBar,
        attrName: String,
        colors: List<String>?
    ) {
        colors?.apply {
            val color = when (attrName) {
                themes_name[3] -> Color.parseColor(colors[0])
                themes_name[4] -> Color.parseColor(colors[0])
                else -> ContextCompat.getColor(view.context, R.color.colorAccent)
            }
            view.setFirstBallColor(color)
            view.setSecondBallColor(color)
        }
    }
}