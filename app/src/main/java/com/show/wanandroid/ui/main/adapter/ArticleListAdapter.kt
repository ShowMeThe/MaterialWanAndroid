package com.show.wanandroid.ui.main.adapter

import android.content.Context
import androidx.databinding.ObservableArrayList
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ItemHomeArticleBinding
import com.show.wanandroid.entity.Article
import showmethe.github.core.adapter.DataBindBaseAdapter

class ArticleListAdapter(context: Context, data: ObservableArrayList<Article.DatasBean>) :
    DataBindBaseAdapter<Article.DatasBean, ItemHomeArticleBinding>(context, data) {
    override fun bindItems(
        binding: ItemHomeArticleBinding?,
        item: Article.DatasBean,
        position: Int
    ) {
        binding?.apply {
            bean = item
            like.setLike(item.isCollect,false)
            like.setOnClickListener {
                if(item.isCollect){
                    like.setLike(boolean = false, state = false)
                }else{
                    like.setLike(boolean = true, state = false)
                }
                item.isCollect = !item.isCollect
                onLikeClick?.invoke(item,item.isCollect)
            }
        }
    }

    override fun getItemLayout(): Int = R.layout.item_home_article

    private var onLikeClick :((item: Article.DatasBean,isCollect:Boolean)->Unit)? = null
    fun setOnLikeClickListener(onLikeClick :((item: Article.DatasBean,isCollect:Boolean)->Unit)){
        this.onLikeClick = onLikeClick
    }
}