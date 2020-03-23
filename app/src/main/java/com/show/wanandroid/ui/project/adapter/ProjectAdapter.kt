package com.show.wanandroid.ui.project.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ItemProjectBinding
import com.show.wanandroid.entity.CateBean
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.glide.TGlide
import showmethe.github.core.glide.loadReveal
import showmethe.github.core.glide.loadScaleNoCrop

/**
 *  com.show.wanandroid.ui.project.adapter
 *  2020/3/23
 *  23:56
 */
class ProjectAdapter(context: Context, data: ObservableArrayList<CateBean.DatasBean>) :
    DataBindBaseAdapter<CateBean.DatasBean, ItemProjectBinding>(context, data) {
    override fun bindItems(binding: ItemProjectBinding?, item: CateBean.DatasBean, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()

            ivCover.loadReveal(item.envelopePic)

            like.setLike(item.isCollect,false)
            like.setOnClickListener {
                if(item.isCollect){
                    like.setLike(boolean = false, state = false)
                }else{
                    like.setLike(boolean = true, state = true)
                }
                item.isCollect = !item.isCollect
                onLikeClick?.invoke(item,item.isCollect)
            }
        }
    }

    override fun getItemLayout(): Int = R.layout.item_project

    private var onLikeClick :((item: CateBean.DatasBean,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: CateBean.DatasBean,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }
}