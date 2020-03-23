package com.show.wanandroid.ui.tree.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.show.wanandroid.R
import com.show.wanandroid.colors
import com.show.wanandroid.databinding.ItemTreeBodyBinding
import com.show.wanandroid.entity.Tree
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import showmethe.github.core.adapter.BaseRecyclerViewAdapter
import showmethe.github.core.adapter.DataBindBaseAdapter
import showmethe.github.core.adapter.DataBindingViewHolder
import showmethe.github.core.base.BaseActivity
import java.util.concurrent.ThreadLocalRandom

/**
 *  com.show.wanandroid.ui.tree.adapter
 *  2020/3/21
 *  11:22
 */
class TreeBodyAdapter(context: Context, data: ObservableArrayList<Tree>) :
    BaseRecyclerViewAdapter<Tree, TreeBodyAdapter.ViewHolder>(context, data) {


    override fun bindDataToItemView(holder: ViewHolder, item: Tree, position: Int) {
        holder.binding.apply {
            bean = item
            executePendingBindings()
            group.removeAllViews()
            if(item.chipChildren.isNotEmpty()){
                 item.chipChildren.forEach {
                     group.addView(it)
                 }
            }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.bind<ItemTreeBodyBinding>(inflateItemView(parent,R.layout.item_tree_body))!!)
    }

    class ViewHolder(binding: ItemTreeBodyBinding) : DataBindingViewHolder<ItemTreeBodyBinding>(binding){
    }
}