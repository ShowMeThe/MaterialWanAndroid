package com.show.kcore.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference


class AppContext private constructor(){


    lateinit var context: Context

    private  var ctx: WeakReference<AppCompatActivity>? = null

    companion object{

        private val instant by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){ AppContext() }

        fun get() = instant

        fun getContext() = instant.context

        fun getActivity() = instant.getActivity()
    }


    fun attach(context: Context){
        this.context = context
    }


    fun attach(activity: AppCompatActivity){
        ctx = WeakReference(activity)
    }

    fun getActivity() = ctx?.get()
}