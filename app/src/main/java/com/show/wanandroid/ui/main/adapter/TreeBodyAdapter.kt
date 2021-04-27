package com.show.wanandroid.ui.main.adapter

import android.content.Context
import android.util.ArrayMap
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import com.google.android.material.chip.Chip
import com.show.kcore.adapter.DataBindBaseAdapter
import com.show.wanandroid.R
import com.show.wanandroid.bean.Tree
import com.show.wanandroid.databinding.ItemTreeBodyBinding


class TreeBodyAdapter(context: Context,var sp : ArrayMap<Int,ArrayList<Chip>>, data: ObservableArrayList<Tree>)
    : DataBindBaseAdapter<Tree,ItemTreeBodyBinding>(context,data){
    override fun getItemLayout(): Int = R.layout.item_tree_body

    override fun bindItems(binding: ItemTreeBodyBinding?, item: Tree, position: Int) {
        binding?.apply {
            bean = item
            executePendingBindings()
            val chips  = sp[position]
            group.removeAllViewsInLayout()
            chips?.forEachIndexed { index, chip ->
                if(chip.parent is ViewGroup){
                    (chip.parent as ViewGroup).removeView(chip)
                }
                group.addView(chip,index)
            }

        }
    }

}