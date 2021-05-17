package com.show.kcore.extras.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


@BindingAdapter("adapter")
fun RecyclerView.adapter(adapter: RecyclerView.Adapter<*>?){
    if(this.adapter == null)
    this.adapter = adapter
}


@BindingAdapter("layoutManager")
fun RecyclerView.layoutManager(layoutManager: RecyclerView.LayoutManager?){
    this.layoutManager = layoutManager
}


@BindingAdapter("itemDecoration")
fun RecyclerView.itemDecoration(itemDecoration: RecyclerView.ItemDecoration?){
    itemDecoration?.apply {
        removeItemDecoration(this)
        addItemDecoration(this)
    }

}


@BindingAdapter("adapter")
fun ViewPager2.adapter(adapter: FragmentStateAdapter?){
    if(this.adapter == null)
    this.adapter = adapter
}

@BindingAdapter("refresh")
fun SwipeRefreshLayout.refresh(boolean: Boolean){
    isRefreshing = boolean
}