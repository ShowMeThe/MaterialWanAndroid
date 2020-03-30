package com.show.wanandroid.plugin

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.show.wanandroid.R
import com.show.wanandroid.themes_name
import com.showmethe.skinlib.plugin.IPlugin

class SearchChipGroup : IPlugin<ChipGroup> {
    override fun individuate(view: ChipGroup, attrName: String) {
        val color = when(attrName){
            themes_name[0] -> ColorStateList.valueOf(Color.parseColor("#5f4fc3f7"))
            themes_name[1] -> ColorStateList.valueOf(Color.parseColor("#5fff4081"))
            themes_name[2] -> ColorStateList.valueOf(Color.parseColor("#5f7c4dff"))
            else -> ColorStateList.valueOf(Color.parseColor("#5f4fc3f7"))
        }
        for(child in view.children){
            val chip = child as Chip
            chip.chipBackgroundColor = color
        }
    }
}