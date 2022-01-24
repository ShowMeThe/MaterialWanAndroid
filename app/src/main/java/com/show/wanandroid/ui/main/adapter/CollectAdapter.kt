package com.show.wanandroid.ui.main.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.glide.TGlide.Companion.load
import com.show.wanandroid.R
import com.show.wanandroid.bean.Collect
import com.show.wanandroid.databinding.ItemCollectBinding
import com.show.wanandroid.widget.swipe.Menu
import com.show.wanandroid.widget.swipe.SwipeMenuAdapter
import com.show.wanandroid.widget.swipe.SwipeMenuLayout

class CollectAdapter(
    context: Context,
    menus: List<Menu>,
) : SwipeMenuAdapter<Collect.DatasBean, ItemCollectBinding>(context, menus) {
    override fun getItemLayout(): Int = R.layout.item_collect

    override fun bindItems(
        viewHolder: SwipeMenuViewHolder<ItemCollectBinding>,
        item: Collect.DatasBean,
        position: Int
    ) {
        viewHolder.binding?.apply {
            bean = item
            executePendingBindings()

            ivCover.load(item.envelopePic)
        }
    }

    override fun bindItemByViewType(
        viewHolder: RecyclerView.ViewHolder,
        viewType: Int,
        position: Int,
        payloads: MutableList<Any>
    ) {

    }

    override fun bindItemsByPayloads(
        viewHolder: RecyclerView.ViewHolder,
        item: Collect.DatasBean,
        position: Int,
        payloads: MutableList<Any>
    ) {

    }
}