package com.show.wanandroid.ui.main.adapter

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import com.show.wanandroid.R
import com.show.wanandroid.const.Data
import com.show.wanandroid.databinding.ItemCollectBinding
import com.show.wanandroid.databinding.ItemProjectBinding
import com.show.wanandroid.entity.Collect
import showmethe.github.core.adapter.slideAdapter.SlideAdapter
import showmethe.github.core.adapter.slideAdapter.SlideViewHolder
import showmethe.github.core.glide.loadReveal

class CollectAdapter(mContext: Context, mData: ObservableArrayList<Collect.DatasBean>) :
    SlideAdapter<Collect.DatasBean>(mContext, mData) {
    override fun getItemLayout(): Int = R.layout.item_collect

    override fun bindItems(holder: SlideViewHolder, item: Collect.DatasBean, position: Int) {
        holder.itemView.apply {
            val binding = DataBindingUtil.bind<ItemCollectBinding>(this)
            binding?.apply {
                bean = item
                executePendingBindings()
                ivCover.visibility = View.GONE
                if(item.envelopePic.isNotEmpty()){
                    ivCover.visibility = View.VISIBLE
                    ivCover.loadReveal(item.envelopePic)
                }else{
                    ivCover.setImageDrawable(null)
                }
            }
        }
    }
}