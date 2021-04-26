package com.show.kcore.adapter.multi

import android.content.Context
import android.util.ArrayMap
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.adapter.DataBindingViewHolder
import com.show.kcore.adapter.createCallback
import java.util.ArrayList

class MultiTypeAdapter<D>(
    val context: Context,
    val data: ObservableArrayList<D>,
    private val adapters: ArrayList<ViewTypeAdapter<D, *>>
) : RecyclerView.Adapter<DataBindingViewHolder<*>>(), LifecycleObserver {

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

    private val viewTypeSaver = ArrayMap<Int, Int>()

    private fun initLife() {
        if (context is LifecycleOwner) {
            context.lifecycle.addObserver(this)
        }
        listener = createCallback(this)
        data.addOnListChangedCallback(listener)
        adapters.forEach {
            it.parentAdapter = this
            it.dataList = data
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (recyclerView.layoutManager != null && recyclerView.layoutManager is GridLayoutManager) {
            val layoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = getItemViewType(position)
                    for (adapter in adapters) {
                        if (adapter.getViewType() == viewType) {
                            return adapter.setSpanSize()
                        }
                    }
                    return 1
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        var viewType = super.getItemViewType(position)
        if (adapters.isNotEmpty()) {
            for (adapter in adapters) {
                if (adapter.isViewTypeLegal(data[position], position)) {
                    viewType = adapter.getViewType()
                    viewTypeSaver[position] = viewType
                    return viewType
                }
            }
        }
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<*> {
        for (adapter in adapters) {
            if (viewType == adapter.getViewType()) {
                return adapter.onCreateViewHolder(parent)
            }
        }
        return super.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<*>, position: Int) {
        val viewType = viewTypeSaver[position]
        for (adapter in adapters) {
            if (viewType == adapter.getViewType()) {
                return adapter.onBindViewHolder(holder, getItemData(position), position)
            }
        }
    }

    private fun getItemData(position: Int) = data[position]

    override fun getItemCount(): Int = data.size
}