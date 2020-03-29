package com.show.wanandroid.ui.main.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ItemHomeArticleBinding
import com.show.wanandroid.entity.Article
import com.showmethe.skinlib.SkinManager
import showmethe.github.core.adapter.BaseRecyclerViewAdapter
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.adapter.DataBindingViewHolder

class ArticleListAdapter(context: Context, data: ObservableArrayList<Article.DatasBean>) :
    BaseRecyclerViewAdapter<Article.DatasBean, ArticleListAdapter.ViewHolder>(context, data) {


    override fun bindDataToItemView(holder: ViewHolder, item: Article.DatasBean, position: Int) {
        holder.binding.apply {
            bean = item
            executePendingBindings()
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

    private var onLikeClick :((item: Article.DatasBean,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: Article.DatasBean,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.bind<ItemHomeArticleBinding>(inflateItemView(parent,
            R.layout.item_home_article))!!
        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)
        return ViewHolder(binding)
    }

    inner class ViewHolder(binding: ItemHomeArticleBinding) :
        DataBindingViewHolder<ItemHomeArticleBinding>(binding){
    }

}