package com.show.wanandroid

import showmethe.github.core.base.AppManager
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.base.ContextProvider
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.toast.ToastFactory


const val motion_delay = 550


fun toast(error: Int, message:String){
    ToastFactory.createToast(message)
    if(error == -1001){

    }
}

