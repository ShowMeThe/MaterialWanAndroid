package com.show.wanandroid.widget.swipe

import android.content.Context
import android.content.res.ColorStateList
import android.util.ArrayMap
import android.util.Log
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
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.show.kcore.adapter.createCallback
import com.show.kcore.extras.binding.Binding

abstract class SwipeMenuAdapter<D, V : ViewBinding>(
    val context: Context,
    private val menus: List<Menu>,
    var data: ObservableArrayList<D>
) :
    RecyclerView.Adapter<SwipeMenuAdapter.SwipeMenuViewHolder<V>>(), LifecycleObserver {

    /**
     * save menu open state
     */
    private val restoreState by lazy { ArrayMap<Int,Boolean>() }

    fun getRestoreStatePool() = restoreState

    fun getRestoreState(int: Int) = restoreState[int]

    fun removeRestoreState(int: Int) = restoreState.remove(int)

    fun clearRestoreState() = restoreState.clear()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeMenuViewHolder<V> {
        val contentBinding =
            Binding.getBinding<V>(this, inflateItemView(parent, getItemLayout()), 1)
        val swipeLayout = createSliderLayout(parent.context, contentBinding!!.root, menus)
        return SwipeMenuViewHolder(swipeLayout, contentBinding)
    }

    override fun onBindViewHolder(holder: SwipeMenuViewHolder<V>, position: Int) {
        val binding = holder.binding
        val swipeLayout = holder.swipeLayout
        restoreState[position]?.apply {
            if(this){
                swipeLayout.openMenu()
            }else{
                swipeLayout.closeMenu()
            }
        }?: swipeLayout.closeMenu()

        swipeLayout.setOnClickListener {
            onItemClick?.invoke(swipeLayout,getItemData(position),position)
        }
        swipeLayout.setOnSwipeChangeListener {
            restoreState[position] = it
        }



        swipeLayout.leftMenuView?.apply {
            children.forEachIndexed { index, view ->
                view.setOnClickListener {
                    onMenuItemClick?.invoke(index,position)
                }
            }
        }
        bindItems(binding, getItemData(position), swipeLayout, position)
    }

    abstract fun getItemLayout(): Int

    abstract fun bindItems(binding: V?, item: D, layout: SwipeMenuLayout, position: Int)

    private fun getItemData(position: Int) = data[position]

    override fun getItemCount(): Int = data.size

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
                setCompoundDrawablesRelativeWithIntrinsicBounds(drawableStart,drawableTop,drawableEnd,drawableBottom)
            }
            compoundDrawablePadding = textMenu.drawablePadding.toInt()
            TextViewCompat.setCompoundDrawableTintList(textView,ColorStateList.valueOf(textMenu.tintColor))
        }
        return textView
    }

    private fun createImage(imageMenu : ImageMenu): View {
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


    private var onMenuItemClick: ((menuPosition: Int,contentPosition:Int) -> Unit)? = null
    fun setOnMenuItemClickListener(onMenuItemClick: ((menuPosition: Int,contentPosition:Int) -> Unit)) {
        this.onMenuItemClick = onMenuItemClick
    }

}