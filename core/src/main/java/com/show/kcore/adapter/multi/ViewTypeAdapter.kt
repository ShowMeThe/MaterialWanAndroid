package com.show.kcore.adapter.multi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.show.kcore.adapter.DataBindingViewHolder
import com.show.kcore.extras.binding.Binding

abstract class ViewTypeAdapter<D, V : ViewBinding> : ViewTypeImp<D, V> {

    lateinit var parentAdapter: MultiTypeAdapter<D>

    lateinit var dataList : List<D>

    override fun onCreateViewHolder(parent: ViewGroup): DataBindingViewHolder<V> {
        val binding = Binding.getBinding<V>(
            this,
            LayoutInflater.from(parent.context).inflate(getViewId(), parent, false),
            1
        )
        return DataBindingViewHolder(binding!!)
    }

    open fun setSpanSize():Int = 1

}