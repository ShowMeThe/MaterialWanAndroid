package com.show.kcore.adapter

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.RecyclerView


fun <D> createCallback(adapter: RecyclerView.Adapter<*>) : ObservableList.OnListChangedCallback<ObservableArrayList<D>>{
    return object : ObservableList.OnListChangedCallback<ObservableArrayList<D>>(){
        override fun onChanged(sender: ObservableArrayList<D>?) {
            adapter. notifyDataSetChanged()
        }
        override fun onItemRangeRemoved(sender: ObservableArrayList<D>?, positionStart: Int, itemCount: Int) {
            if (itemCount == 1) {
                adapter. notifyItemRemoved(positionStart)
                adapter. notifyItemRangeChanged(positionStart, itemCount)
            } else {
                adapter. notifyDataSetChanged()
            }
        }

        override fun onItemRangeMoved(sender: ObservableArrayList<D>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            if (itemCount == 1) {
                adapter.  notifyItemMoved(fromPosition, toPosition)
            } else {
                adapter. notifyDataSetChanged()
            }
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<D>, positionStart: Int, itemCount: Int) {
            adapter.  notifyItemInserted(positionStart + 1)
            adapter. notifyItemRangeChanged(positionStart , sender.size - positionStart)


        }

        override fun onItemRangeChanged(sender: ObservableArrayList<D>?, positionStart: Int, itemCount: Int) {
            adapter. notifyItemRangeChanged(positionStart, itemCount)
        }

    }
}