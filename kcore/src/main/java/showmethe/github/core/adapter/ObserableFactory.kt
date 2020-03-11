package showmethe.github.core.adapter

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import showmethe.github.core.base.BaseApplication
import showmethe.github.core.base.ContextProvider

/**
 * Author: showMeThe
 * Update Time: 2019/11/6 13:36
 * Package Name:showmethe.github.core.adapter
 */


fun <D> ObservableArrayList<D>.addCallback(adapter: RecyclerView.Adapter<*>){
    addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<D>>(){
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

    })
}