package com.show.wanandroid.widget.swipe

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.widget.TextViewCompat
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.show.kcore.extras.binding.Binding

abstract class SwipeMenuAdapter<D, V : ViewBinding>(
    val context: Context,
    private val menus: List<Menu>,
) :
    RecyclerView.Adapter<RecyclerView
    .ViewHolder>(), LifecycleObserver {

    private val mData = ArrayList<DataWrapper<D>>()

    protected data class DataWrapper<D>(var data: D, var isOpen: Boolean = false)

    protected fun getCurrentData() = mData

    fun submitList(list: MutableList<D>) {
        mData.clear()
        mData.addAll(list.map { DataWrapper(it, false) })
        notifyDataSetChanged()
    }

    fun add(data: D) {
        mData.add(DataWrapper(data))
        notifyItemInserted((mData.size - 1).coerceAtLeast(0))
    }

    fun add(index: Int, data: D) {
        mData.add(index, DataWrapper(data))
        notifyItemInserted(index)
        notifyItemRangeChanged(index, mData.size - index)
    }

    fun addAll(list: Collection<D>) {
        val lastPosition = (mData.size - 1).coerceAtLeast(0)
        mData.addAll(list.map { DataWrapper(it) })
        val lastPositionNext = mData.size - 1 - lastPosition
        notifyItemRangeInserted(lastPositionNext, mData.size - lastPositionNext)
    }

    fun removeAt(position: Int) {
        if (position != -1) {
            mData.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mData.size - position)
        }
    }

    fun removeItem(data: D) {
        val position = mData.indexOf(DataWrapper(data))
        if (position != -1) {
            mData.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mData.size - position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val contentBinding =
            Binding.getBinding<V>(this, inflateItemView(parent, getItemLayout()), 1)
        val swipeLayout = createSliderLayout(parent.context, contentBinding!!.root, menus)
        return SwipeMenuViewHolder(swipeLayout, contentBinding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is SwipeMenuViewHolder<*>) {
            val swipeLayout = viewHolder.swipeLayout

            val item = getItem(viewHolder.layoutPosition)
            if (item.isOpen) {
                swipeLayout.openMenu()
            } else {
                swipeLayout.closeMenu()
            }

            swipeLayout.setOnClickListener {
                onItemClick?.invoke(
                    swipeLayout,
                    getItemData(viewHolder.layoutPosition),
                    viewHolder.layoutPosition
                )
            }
            swipeLayout.setOnSwipeChangeListener {
                if (it) {
                    mData.find { i -> i.isOpen }?.apply {
                        val lastOpenPosition = mData.indexOf(this)
                        if (lastOpenPosition != position) {
                            this.isOpen = false
                            notifyItemChanged(lastOpenPosition, this.isOpen)
                        }
                    }
                }
                item.isOpen = it
            }

            swipeLayout.leftMenuView?.apply {
                children.forEachIndexed { index, view ->
                    view.setOnClickListener {
                        onMenuItemClick?.invoke(index, viewHolder.layoutPosition)
                    }
                }
            }
            bindItems(viewHolder as SwipeMenuViewHolder<V>, item.data, viewHolder.layoutPosition)
        } else {
            bindItemByViewType(viewHolder, getItemViewType(viewHolder.layoutPosition),viewHolder.layoutPosition,mutableListOf())
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
           onBindViewHolder(holder, position)
        } else {
            if(holder is SwipeMenuAdapter.SwipeMenuViewHolder<*>){
                if (payloads[0] is Boolean) {
                    val result = payloads[0] as Boolean
                    if (result) {
                        holder.swipeLayout.smoothOpenMenu(notifyChange = false)
                    } else {
                        holder.swipeLayout.smoothCloseMenu(notifyChange = false)
                    }
                }
            }
            bindItemsByPayloads(holder, getItem(holder.layoutPosition).data, position, payloads)
        }
    }

    abstract fun getItemLayout(): Int

    abstract fun bindItems(
        viewHolder: SwipeMenuViewHolder<V>,
        item: D,
        position: Int
    )

    abstract fun bindItemByViewType( viewHolder: RecyclerView.ViewHolder,
                                     viewType: Int,
                                     position: Int,payloads: MutableList<Any>)

    abstract fun bindItemsByPayloads(
        viewHolder: RecyclerView.ViewHolder,
        item: D,
        position: Int,
        payloads: MutableList<Any>
    )

    private fun getItemData(position: Int) = mData[position].data

    private fun getItem(position: Int) = mData[position]

    override fun getItemCount(): Int = mData.size

    private fun inflateItemView(viewGroup: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }

    class SwipeMenuViewHolder<V>(val swipeLayout: SwipeMenuLayout, val binding: V?) :
        RecyclerView.ViewHolder(swipeLayout)


    private var onItemClick: ((view: View, data: D, position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener: (view: View, data: D, position: Int) -> Unit) {
        this.onItemClick = onItemClickListener
    }


    private fun createSliderLayout(
        context: Context,
        contentView: View,
        menus: List<Menu>
    ): SwipeMenuLayout {
        val swipeLayout = SwipeMenuLayout(context)
        swipeLayout.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        swipeLayout.addView(contentView)
        val menusLayout = LinearLayout(context)
        menusLayout.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        menusLayout.tag = "leftMenu"
        menusLayout.orientation = LinearLayout.HORIZONTAL
        menus.forEachIndexed { index, it ->
            if (it is TextMenu) {
                menusLayout.addView(createText(it))
            } else if (it is ImageMenu) {
                menusLayout.addView(createImage(it))
            }
        }
        swipeLayout.addView(menusLayout)
        return swipeLayout
    }

    private fun createText(textMenu: TextMenu): View {
        val textView = TextView(context)
        textView.apply {
            setBackgroundColor(textMenu.backgroundColor)
            text = textMenu.text
            gravity = Gravity.CENTER
            setTextColor(textMenu.textColor)
            textSize = textMenu.textSize
            layoutParams = LinearLayout.LayoutParams(
                textMenu.menuWidth.toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            ).also {
                val padding = textMenu.menuPadding.toInt()
                setPadding(padding, padding, padding, padding)
            }
            textMenu.apply {
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawableStart,
                    drawableTop,
                    drawableEnd,
                    drawableBottom
                )
            }
            compoundDrawablePadding = textMenu.drawablePadding.toInt()
            TextViewCompat.setCompoundDrawableTintList(
                textView,
                ColorStateList.valueOf(textMenu.tintColor)
            )
        }
        return textView
    }

    private fun createImage(imageMenu: ImageMenu): View {
        val imageView = ImageView(context)
        imageView.apply {
            setBackgroundColor(imageMenu.backgroundColor)
            setImageDrawable(imageMenu.drawable)
            setColorFilter(imageMenu.tintColor)

            layoutParams = LinearLayout.LayoutParams(
                imageMenu.menuWidth.toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            ).also {
                val padding = imageMenu.menuPadding.toInt()
                setPadding(padding, padding, padding, padding)
            }
        }
        return ImageView(context)
    }


    private var onMenuItemClick: ((menuPosition: Int, contentPosition: Int) -> Unit)? = null
    fun setOnMenuItemClickListener(onMenuItemClick: ((menuPosition: Int, contentPosition: Int) -> Unit)) {
        this.onMenuItemClick = onMenuItemClick
    }

}