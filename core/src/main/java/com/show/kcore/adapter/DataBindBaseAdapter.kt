package com.show.kcore.adapter

import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import com.show.kcore.extras.binding.Binding
import com.show.kcore.extras.gobal.setOnSingleClickListener


abstract class DataBindBaseAdapter<D, V : ViewBinding>(val context: Context,
                                                           var data: ObservableArrayList<D>) : RecyclerView.Adapter<DataBindingViewHolder<V>>(), LifecycleObserver {

    init {
        initLife()
    }

    private lateinit var listener: ObservableList.OnListChangedCallback<ObservableArrayList<D>>
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (context is LifecycleOwner) {
            context.lifecycle.removeObserver(this)
        }
        data.removeOnListChangedCallback(listener)
    }

    private fun initLife() {
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(this)
        }
        listener = createCallback(this)
        data.addOnListChangedCallback(listener)
    }


    override fun getItemId(position: Int): Long {
        return if(data.isEmpty()) data[position].hashCode().toLong() else 0
    }


    override fun onBindViewHolder(holder: DataBindingViewHolder<V>, position: Int) {
        holder.itemView.setOnSingleClickListener {
            onItemClick?.invoke(it,data[position],holder.layoutPosition)
        }
        bindItems(holder.binding, data[position], position)
    }

    abstract fun getItemLayout() : Int

    abstract fun bindItems(binding: V?, item: D, position: Int)



    private fun inflateItemView(viewGroup: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<V> {
        val binding = Binding.getBinding<V>(this,inflateItemView(parent, getItemLayout()),1)
        return DataBindingViewHolder<V>(binding!!)
    }


    override fun getItemCount(): Int = data.size

    private var onItemClick : ((view: View,data:D, position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener:(view: View,data:D,position: Int) -> Unit) {
        this.onItemClick = onItemClickListener
    }




}
