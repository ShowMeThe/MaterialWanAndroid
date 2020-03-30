package com.show.wanandroid.plugin

import androidx.core.content.ContextCompat
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.show.wanandroid.widget.WaveView
import com.showmethe.skinlib.plugin.IPlugin

class WaveIPlugin : IPlugin<WaveView>{
    override fun individuate(view: WaveView, attrName: String) {
        when(attrName){
            themes_name[0]-> view.setWaveColor(ContextCompat.getColor(view.context,R.color.colorAccent))
            themes_name[1] ->   view.setWaveColor(ContextCompat.getColor(view.context,R.color.color_ff4081))
            themes_name[2] ->  view.setWaveColor(ContextCompat.getColor(view.context,R.color.color_7c4dff))
        }
    }

}