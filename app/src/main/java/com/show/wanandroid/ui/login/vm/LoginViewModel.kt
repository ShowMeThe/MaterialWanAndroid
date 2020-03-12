package com.show.wanandroid.ui.login.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import showmethe.github.core.base.BaseViewModel

class LoginViewModel(application: Application) : BaseViewModel(application) {

    val replaceFragment = MutableLiveData<Int>()
    val backPress = MutableLiveData<Boolean>()

    override fun onViewModelCreated(owner: LifecycleOwner) {

    }
}