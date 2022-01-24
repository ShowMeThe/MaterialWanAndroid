package com.show.kcore.adapter.multi

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.show.kcore.adapter.DataBindingViewHolder

interface ViewTypeImp<D, V : ViewBinding> {

    fun getViewId(): Int

    fun getViewType(): Int

    fun isViewTypeLegal(data: D, position: Int): Boolean

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, data: D, position: Int)

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
}