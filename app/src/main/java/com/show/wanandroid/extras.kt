package com.show.wanandroid

import com.show.wanandroid.const.HAS_LOGIN
import showmethe.github.core.base.AppManager
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.base.ContextProvider
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.toast.ToastFactory


const val motion_delay = 550L
val colors = arrayListOf("#f48fb1","#ce93d8","#b39ddb","#81d4fa","#a5d6a7","#ffab91","#ffe082","#bcaaa4")

fun toast(error: Int, message:String){
    ToastFactory.createToast(message)
    if(error == -1001){

    }
    RDEN.put("sessionId","")
    RDEN.put(HAS_LOGIN,false)
    RetroHttp.get().headerInterceptor.update("")
}

