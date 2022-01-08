package com.show.kcore.adapter

import android.content.Context
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.show.kcore.extras.binding.Binding
import com.show.kcore.extras.gobal.setOnSingleClickListener


abstract class DataBindBaseAdapter<D, V : ViewBinding>(
    val context: Context,
    var data: ObservableArrayList<D>
) : RecyclerView.Adapter<DataBindingViewHolder<V>>(), LifecycleObserver {



    private val listener: ObservableList.OnListChangedCallback<ObservableArrayList<D>> by lazy {
        createCallback(
            this
        )
    }

    private val lifecycleListener by lazy {
        LifecycleEventObserver { _, event ->
            if (context is LifecycleOwner && event == Lifecycle.Event.ON_DESTROY) {
                data.removeOnListChangedCallback(listener)
                context.lifecycle.removeObserver(this)
            }
        }
    }

    private fun initLife() {
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(lifecycleListener)
        }
        data.addOnListChangedCallback(listener)
    }

    init {
        initLife()
    }

    override fun getItemId(position: Int): Long {
        return if (data.isEmpty()) data[position].hashCode().toLong() else 0
    }


    override fun onBindViewHolder(holder: DataBindingViewHolder<V>, position: Int) {
        val itemData = data[holder.bindingAdapterPosition]
        holder.itemView.setOnSingleClickListener {
            onItemClick?.invoke(it, itemData, holder.layoutPosition)
        }
        bindItems(holder.binding, itemData, holder.layoutPosition)
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<V>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder,position,payloads)
        } else {
            bindItemsByPayloads(holder.binding, position, payloads)
        }
    }

    abstract fun getItemLayout(): Int

    abstract fun bindItems(binding: V?, item: D, position: Int)

    protected fun bindItemsByPayloads(binding: V?, position: Int, payloads: MutableList<Any>) {}

    private fun inflateItemView(viewGroup: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<V> {
        val binding = Binding.getBinding<V>(this, inflateItemView(parent, getItemLayout()), 1)
        return DataBindingViewHolder<V>(binding!!)
    }

    override fun getItemCount(): Int = data.size

    private var onItemClick: ((view: View, data: D, position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: (view: View, data: D, position: Int) -> Unit) {
        this.onItemClick = onItemClickListener
    }


}
