package com.show.wanandroid.ui.main.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.adapter.BaseRecyclerViewAdapter
import com.show.kcore.adapter.DataBindBaseAdapter
import com.show.kcore.adapter.DataBindingViewHolder
import com.show.kcore.adapter.DataDifferBaseAdapter
import com.show.kcore.glide.TGlide.Companion.load
import com.show.wanandroid.CONFIG
import com.show.wanandroid.R
import com.show.wanandroid.SCALE_CONFIG
import com.show.wanandroid.bean.CateBean
import com.show.wanandroid.bean.Data
import com.show.wanandroid.databinding.ItemHomeArticleBinding
import com.show.wanandroid.databinding.ItemProjectBinding
import com.showmethe.skinlib.SkinManager


class ProjectAdapter(context: Context, diffCallback: DiffUtil.ItemCallback<Data>,) :
    DataDifferBaseAdapter<Data, ItemProjectBinding>(context, diffCallback) {

    override fun getItemLayout(): Int = R.layout.item_project


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<ItemProjectBinding> {
        val binding = DataBindingUtil.bind<ItemProjectBinding>(inflateItemView(parent,getItemLayout()))!!
        SkinManager.getManager().autoTheme(SkinManager.currentStyle,binding)
        return DataBindingViewHolder(binding)
    }


    private var onLikeClick :((item: Data,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: Data,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }

    override fun bindItems(binding: ItemProjectBinding?, item: Data, position: Int) {
        binding?.apply {
            Log.e("22222","$item")
            bean = item
            executePendingBindings()

            ivCover.load(item.envelopePic, SCALE_CONFIG)

        }
    }


}