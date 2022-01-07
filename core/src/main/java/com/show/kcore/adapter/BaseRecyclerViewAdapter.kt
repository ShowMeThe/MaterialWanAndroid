package com.show.kcore.adapter

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.show.kcore.extras.gobal.setOnSingleClickListener
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type



abstract class BaseRecyclerViewAdapter<D, V : RecyclerView.ViewHolder>(
    val context: Context,
    var data: ObservableArrayList<D>
) : RecyclerView.Adapter<V>(), LifecycleObserver{

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

    private var onItemClick : ((view: View,data:D, position: Int) -> Unit)? = null

    fun setOnItemClickListener(onItemClickListener:(view: View, data:D,position: Int) -> Unit) {
        this.onItemClick = onItemClickListener
    }


    fun inflateItemView(viewGroup: ViewGroup, layoutId: Int): View {
        return LayoutInflater.from(viewGroup.context).inflate(layoutId, viewGroup, false)
    }

    abstract fun getItemLayout() : Int

    protected abstract fun bindItems(holder: V, item: D, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
        val clazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val vClazz = getVClazz(clazz)
        return if(vClazz == null){
            BaseViewHolder(inflateItemView(parent,getItemLayout())) as V
        }else{
            return if(vClazz.isMemberClass && !Modifier.isStatic(vClazz.modifiers)){
                val constructor = vClazz.getDeclaredConstructor(javaClass,View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(this,inflateItemView(parent,getItemLayout())) as V
            }else{
                val constructor = vClazz.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(inflateItemView(parent,getItemLayout())) as V
            }
        }
    }

    class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun getVClazz(array: Array<Type>):Class<*>?{
        val viewHolderType = array[1]  as Class<*>
        return if(RecyclerView.ViewHolder::class.java.isAssignableFrom(viewHolderType)){
            viewHolderType
        }else{
            null
        }
    }


    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long {
        return if(data.isEmpty()) data[position].hashCode().toLong() else 0
    }

    override fun onBindViewHolder(viewHolder: V, position: Int) {
        viewHolder.itemView.setOnSingleClickListener { v ->
            onItemClick?.invoke(v, data[viewHolder.layoutPosition],viewHolder.layoutPosition)
        }


        val item = data[viewHolder.layoutPosition]
        bindItems(viewHolder, item, viewHolder.layoutPosition)


    }

}

