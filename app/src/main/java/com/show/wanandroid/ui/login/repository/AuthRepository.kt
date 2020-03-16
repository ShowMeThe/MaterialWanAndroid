package com.show.wanandroid.ui.login.repository

import androidx.lifecycle.MutableLiveData
import com.ken.materialwanandroid.entity.Empty
import com.show.wanandroid.api.Main
import com.show.wanandroid.entity.Auth
import com.show.wanandroid.toast
import showmethe.github.core.base.BaseRepository
import showmethe.github.core.http.coroutines.CallResult
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.kinit.inject
import showmethe.github.core.util.extras.post

class AuthRepository : BaseRepository() {
    val api : Main by inject()

    fun login(username:String,password:String,call: MutableLiveData<Result<Auth>>){
        CallResult<Auth>(owner){
            post(call)
            loading {
                showLoading()
            }
            success { result, message ->
                dismissLoading()
            }
            error { result, code, message ->
                toast(code,message)
            }
            hold {
                api.login(username, password)
            }
        }
    }

    fun register(username:String,password:String,call:MutableLiveData<Result<Auth>>){
        CallResult<Empty>(owner){
            error { result, code, message ->
                toast(code,message)
            }
            success { result, message ->
                login(username,password,call)
            }
            hold {
                api.register(username, password,password)
            }
        }

    }
}