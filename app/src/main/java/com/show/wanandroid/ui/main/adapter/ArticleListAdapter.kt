package com.show.wanandroid.ui.main.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.adapter.BaseRecyclerViewAdapter
import com.show.wanandroid.R
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.databinding.ItemHomeArticleBinding
import com.showmethe.skinlib.SkinManager

class ArticleListAdapter(context: Context, data: ObservableArrayList<DatasBean>) :
    BaseRecyclerViewAdapter<DatasBean, ArticleListAdapter.ViewHolder>(context, data) {

    override fun getItemLayout(): Int = R.layout.item_home_article

    override fun bindItems(holder: ViewHolder, item: DatasBean, position: Int) {
        holder.binding.apply {
            bean = item
            executePendingBindings()


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.bind<ItemHomeArticleBinding>(inflateItemView(parent,getItemLayout()))!!
        SkinManager.getManager().autoTheme(SkinManager.currentStyle,binding)
        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemHomeArticleBinding) : RecyclerView.ViewHolder(binding.root)

    private var onLikeClick: ((item: DatasBean, isCollect: Boolean) -> Unit)? = null
    fun setOnLikeClickListener(onLikeClick: ((item: DatasBean, isCollect: Boolean) -> Unit)) {
        this.onLikeClick = onLikeClick
    }


}