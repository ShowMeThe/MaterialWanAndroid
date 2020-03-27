package com.example.ken.kmvvm.skin

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import com.showmethe.skinlib.SkinManager


/**
 * Author: showMeThe
 * Update Time: 2020/1/17 10:41
 * Package Name:com.example.ken.kmvvm.skin
 */

val TAG = "SkinManager"

@BindingAdapter("skin")
fun View.skin(url:String){
    Log.d(TAG,this::class.java.name)
    SkinManager.patchView(this,url)
}

@BindingAdapter("skin")
fun ViewGroup.skin(url:String){
    Log.d(TAG,this::class.java.name)
    SkinManager.patchView(this,url)
}