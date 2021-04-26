package com.show.wanandroid.ui.main.adapter

import android.content.Context
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.adapter.BaseRecyclerViewAdapter
import com.show.wanandroid.R
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.databinding.ItemHomeArticleBinding

class ArticleListAdapter(context: Context, data: ObservableArrayList<DatasBean>) :
    BaseRecyclerViewAdapter<DatasBean, ArticleListAdapter.ViewHolder>(context, data) {

    override fun getItemLayout(): Int = R.layout.item_home_article

    override fun bindItems(holder: ViewHolder, item: DatasBean, position: Int) {
        val binding = DataBindingUtil.bind<ItemHomeArticleBinding>(holder.itemView)
        binding?.apply {
            bean = item
            executePendingBindings()


        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onLikeClick: ((item: DatasBean, isCollect: Boolean) -> Unit)? = null
    fun setOnLikeClickListener(onLikeClick: ((item: DatasBean, isCollect: Boolean) -> Unit)) {
        this.onLikeClick = onLikeClick
    }


}