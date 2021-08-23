package com.show.kcore.extras.binding

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.show.kcore.BuildConfig
import java.lang.ref.SoftReference
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class ViewBindRef<T : ViewBinding> : ReadWriteProperty<Any, T>,
    LifecycleObserver {

    private var binding: T? = null
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "$thisRef  Set ViewBinding $value")
        }
        binding = value
        if (thisRef is LifecycleOwner) {
            if (thisRef is Fragment) {
                thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
            } else {
                thisRef.lifecycle.addObserver(this)
            }
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = binding
        ?: throw IllegalAccessException("Do not try to call the ViewDataBinding outside the Activity view lifecycle")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "onDestroy ViewDataBindRef")
        }
        binding = null
    }
}

class ViewDataBindRef<T : ViewBinding> : ReadWriteProperty<Any, T>,LifecycleObserver {

    private var binding: T? = null
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "$thisRef  Set DataBinding $value")
        }
        binding = value
        if (thisRef is LifecycleOwner) {
            if (binding is ViewDataBinding) {
                (binding as ViewDataBinding).lifecycleOwner = thisRef
            }
            if (thisRef is Fragment) {
                thisRef.viewLifecycleOwner.lifecycle.addObserver(this)
            } else {
                thisRef.lifecycle.addObserver(this)
            }
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = binding
        ?: throw IllegalAccessException("Do not try to call the ViewDataBinding outside the Activity view lifecycle")

    fun onDestroyView() {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "onDestroy ViewDataBindRef")
        }
        binding = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (BuildConfig.DEBUG) {
            Log.e("DataBinding", "onDestroy ViewDataBindRef")
        }
        binding = null
    }
}

class DialogFragmentRef<T : DialogFragment>(private val clazz: Class<T>) : Lazy<T> {

    private var soft: SoftReference<T>? = null

    override val value: T
        get() = if (soft != null && soft?.get() != null) {
            soft?.get()!!
        } else {
            soft = SoftReference(clazz.newInstance())
            soft?.get()!!
        }

    override fun isInitialized(): Boolean {
        return (soft == null || soft?.get() == null)
    }
}