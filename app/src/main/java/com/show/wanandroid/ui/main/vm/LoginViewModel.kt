package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ken.materialwanandroid.entity.Empty
import com.show.kcore.http.coroutines.KResult
import com.show.wanandroid.bean.Auth
import com.show.wanandroid.bean.JsonData
import com.show.wanandroid.bean.UserBean
import com.show.wanandroid.ui.main.repository.LoginRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val registerBean  = UserBean()
    var loginBean = UserBean()
    val repository by lazy { LoginRepository(this) }
    val login by lazy { MutableSharedFlow<KResult<JsonData<Auth>>>() }
    val register by lazy { MutableSharedFlow<KResult<JsonData<Empty>>>() }

    fun loginIn(){
        repository.login(loginBean.account,loginBean.password,login)
    }

    fun register(){
        repository.register(registerBean.account,registerBean.password,register,login)
    }

}