package com.show.wanandroid.ui.login.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.ken.materialwanandroid.entity.Empty
import com.show.wanandroid.entity.Auth
import com.show.wanandroid.entity.UserBean
import com.show.wanandroid.ui.login.repository.AuthRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath
import showmethe.github.core.http.coroutines.Result

class LoginViewModel(application: Application) : BaseViewModel(application) {

    @InjectOwner
    val repository = AuthRepository()

    val registerBean  = UserBean()
    val loginBean = UserBean()
    val register = MutableLiveData<Boolean>()
    val auth = MutableLiveData<Result<Auth>>()
    val replaceFragment = MutableLiveData<Int>()
    val backPress = MutableLiveData<Boolean>()

    override fun onViewModelCreated(owner: LifecycleOwner) {

    }

    /**
     * 登录
     */
    @VMPath("login")
    fun login(){
        repository.login(loginBean.account, loginBean.password, auth)
    }

    /**
     * 注册
     */
    @VMPath("register")
    fun register(){
        repository.register(registerBean.account, registerBean.password, auth)
    }
}