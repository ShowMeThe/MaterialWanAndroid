package com.show.wanandroid

import android.content.Intent
import com.show.wanandroid.const.HAS_LOGIN
import com.show.wanandroid.ui.login.LoginActivity
import showmethe.github.core.base.AppManager
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.base.ContextProvider
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.toast.ToastFactory


val themes_res = arrayListOf(R.color.colorAccent,R.color.color_304ffe,R.color.color_6200ea)
val themes_name = arrayListOf("BlueTheme","RedTheme","PurpleTheme")

const val motion_delay = 550L
val colors = arrayListOf("#f48fb1","#ce93d8","#b39ddb","#81d4fa","#a5d6a7","#ffab91","#ffe082","#bcaaa4")

fun toast(error: Int, message:String){
    ToastFactory.createToast(message)
    if(error == -1001){
        RDEN.put("sessionId","")
        RDEN.put(HAS_LOGIN,false)
        RetroHttp.get().headerInterceptor.update("")
        val current = ContextProvider.get().getActivity()
        val intent = Intent(current,LoginActivity::class.java)
        current?.startActivity(intent)
    }
}

