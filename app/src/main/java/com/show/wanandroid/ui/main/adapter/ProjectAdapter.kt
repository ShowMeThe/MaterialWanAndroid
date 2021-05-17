package com.show.wanandroid.ui.main.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.adapter.BaseRecyclerViewAdapter
import com.show.kcore.adapter.DataBindBaseAdapter
import com.show.kcore.adapter.DataBindingViewHolder
import com.show.kcore.glide.TGlide.Companion.load
import com.show.wanandroid.CONFIG
import com.show.wanandroid.R
import com.show.wanandroid.SCALE_CONFIG
import com.show.wanandroid.bean.CateBean
import com.show.wanandroid.bean.Data
import com.show.wanandroid.databinding.ItemHomeArticleBinding
import com.show.wanandroid.databinding.ItemProjectBinding
import com.showmethe.skinlib.SkinManager


class ProjectAdapter(context: Context, data: ObservableArrayList<Data>) :
    BaseRecyclerViewAdapter<Data, ProjectAdapter.ViewHolder>(context, data) {

    override fun getItemLayout(): Int = R.layout.item_project

    override fun bindItems(holder : ViewHolder, item: Data, position: Int) {
        holder.binding.apply {
            bean = item
            executePendingBindings()

            ivCover.load(item.envelopePic, SCALE_CONFIG)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.bind<ItemProjectBinding>(inflateItemView(parent,getItemLayout()))!!
        SkinManager.getManager().autoTheme(SkinManager.currentStyle,binding)
        return ViewHolder(binding)
    }

    class ViewHolder(binding: ItemProjectBinding) :
        DataBindingViewHolder<ItemProjectBinding>(binding)

    private var onLikeClick :((item: Data,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: Data,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }


}