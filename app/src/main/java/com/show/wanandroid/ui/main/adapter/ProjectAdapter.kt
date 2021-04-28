package com.show.wanandroid.ui.main.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import com.show.kcore.adapter.DataBindBaseAdapter
import com.show.kcore.glide.TGlide.Companion.load
import com.show.wanandroid.CONFIG
import com.show.wanandroid.R
import com.show.wanandroid.SCALE_CONFIG
import com.show.wanandroid.bean.CateBean
import com.show.wanandroid.bean.Data
import com.show.wanandroid.databinding.ItemProjectBinding


class ProjectAdapter(context: Context, data: ObservableArrayList<Data>) :
    DataBindBaseAdapter<Data, ItemProjectBinding>(context, data) {

    override fun getItemLayout(): Int = R.layout.item_project

    override fun bindItems(binding: ItemProjectBinding?, item: Data, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()

            ivCover.load(item.envelopePic, SCALE_CONFIG)

        }
    }


    private var onLikeClick :((item: Data,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: Data,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }


}