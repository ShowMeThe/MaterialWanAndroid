package com.show.wanandroid.dialog.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import com.show.kcore.adapter.DataBindBaseAdapter
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ItemThemeSelectBinding

class ThemeAdapter(context: Context, data: ObservableArrayList<Int>) :
    DataBindBaseAdapter<Int, ItemThemeSelectBinding>(context, data) {

    override fun bindItems(binding: ItemThemeSelectBinding?, item: Int, position: Int) {
        binding?.apply {
            shape.shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED,35f)
                .build()
            shape.setImageDrawable(ColorDrawable(ContextCompat.getColor(context,item)))
        }
    }

    override fun getItemLayout(): Int = R.layout.item_theme_select
}