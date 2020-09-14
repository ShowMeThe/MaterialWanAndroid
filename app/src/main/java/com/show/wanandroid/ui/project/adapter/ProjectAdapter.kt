package com.show.wanandroid.ui.project.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ItemProjectBinding
import com.show.wanandroid.entity.CateBean
import com.showmethe.skinlib.SkinManager
import showmethe.github.core.adapter.BaseRecyclerViewAdapter
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.adapter.DataBindingViewHolder
import showmethe.github.core.glide.TGlide
import showmethe.github.core.glide.TGlide.Companion.load

/**
 *  com.show.wanandroid.ui.project.adapter
 *  2020/3/23
 *  23:56
 */
class ProjectAdapter(context: Context, data: ObservableArrayList<CateBean.DatasBean>) :
    BaseRecyclerViewAdapter<CateBean.DatasBean, ProjectAdapter.ViewHolder>(context, data) {

    private val config = TGlide.Config.newConfig().apply {
        isReveal = true
        isViewTarget = true
    }

    override fun bindDataToItemView(holder: ViewHolder, item: CateBean.DatasBean, position: Int) {
        holder.binding.apply {
            bean = item
            executePendingBindings()

            ivCover.load(item.envelopePic,config)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.bind<ItemProjectBinding>(inflateItemView(parent,R.layout.item_project))
        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)
        return ViewHolder(binding!!)
    }

    inner class ViewHolder(binding: ItemProjectBinding) :
        DataBindingViewHolder<ItemProjectBinding>(binding)

    private var onLikeClick :((item: CateBean.DatasBean,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: CateBean.DatasBean,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }


}