package com.show.kcore.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.show.kcore.extras.binding.Binding
import com.show.kcore.extras.gobal.setOnSingleClickListener
import java.util.*


abstract class DataDifferBaseAdapter<D, V : ViewBinding>(val context: Context,
                                                           diffCallback: DiffUtil.ItemCallback<D>)
    : ListAdapter<D, DataBindingViewHolder<V>>(diffCallback) {

    override fun submitList(list: MutableList<D>?) {
        super.submitList(list){
            super.submitList(if(list.isNullOrEmpty()){
                ArrayList()
            }else{
                ArrayList(list)
            })
        }
    }


    override fun onBindViewHolder(holder: DataBindingViewHolder<V>, position: Int) {
        holder.itemView.setOnSingleClickListener {
            onItemClick?.invoke(it,getItem(holder.layoutPosition),holder.layoutPosition)
        }
        bindItems(holder.binding,getItem(holder.layoutPosition), holder.layoutPosition)
    }

    abstract fun getItemLayout() : Int

    abstract fun bindItems(binding: V?, item: D, position: Int)



    protected fun inflateItemView(viewGroup: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<V> {
        val binding = Binding.getBinding<V>(this,inflateItemView(parent, getItemLayout()),1)
        return DataBindingViewHolder<V>(binding!!)
    }


    private var onItemClick : ((view: View,data:D,position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener:(view: View,data:D,position: Int) -> Unit) {
        this.onItemClick = onItemClickListener
    }




}
