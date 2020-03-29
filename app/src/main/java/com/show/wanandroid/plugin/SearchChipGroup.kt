package com.show.wanandroid.plugin

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.show.wanandroid.R
import com.showmethe.skinlib.plugin.IPlugin

class SearchChipGroup : IPlugin<ChipGroup> {
    override fun individuate(view: ChipGroup, attrName: String) {
        val color = when(attrName){
            "BlueTheme" -> ColorStateList.valueOf(Color.parseColor("#5f4fc3f7"))
            "RedTheme" -> ColorStateList.valueOf(Color.parseColor("#5fff4081"))
            else -> ColorStateList.valueOf(Color.parseColor("#5f4fc3f7"))
        }
        for(child in view.children){
            val chip = child as Chip
            chip.chipBackgroundColor = color
        }
    }
}