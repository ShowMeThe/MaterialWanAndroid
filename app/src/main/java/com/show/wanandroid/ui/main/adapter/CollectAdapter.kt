package com.show.wanandroid.ui.main.adapter

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableArrayList
import com.show.kcore.glide.TGlide.Companion.load
import com.show.wanandroid.widget.swipe.SwipeMenuLayout
import com.show.wanandroid.R
import com.show.wanandroid.bean.Collect
import com.show.wanandroid.databinding.ItemCollectBinding
import com.show.wanandroid.widget.swipe.Menu
import com.show.wanandroid.widget.swipe.SwipeMenuAdapter

class CollectAdapter(
    context: Context,
    menus: List<Menu>,
    data: ObservableArrayList<Collect.DatasBean>
) : SwipeMenuAdapter<Collect.DatasBean, ItemCollectBinding>(context, menus, data) {
    override fun getItemLayout(): Int = R.layout.item_collect

    override fun bindItems(
        binding: ItemCollectBinding?,
        item: Collect.DatasBean,
        layout: SwipeMenuLayout,
        position: Int
    ) {
        binding?.apply {
            bean = item
            executePendingBindings()

            ivCover.load(item.envelopePic)
        }
    }
}