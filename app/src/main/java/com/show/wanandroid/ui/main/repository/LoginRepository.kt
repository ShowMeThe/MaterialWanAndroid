package com.show.wanandroid.ui.main.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ken.materialwanandroid.entity.Empty
import com.show.kInject.core.ext.inject
import com.show.kInject.core.ext.single
import com.show.kcore.base.BaseRepository
import com.show.kcore.http.coroutines.KResult
import com.show.kcore.http.coroutines.callResult
import com.show.wanandroid.api.Main
import com.show.wanandroid.bean.Auth
import com.show.wanandroid.bean.JsonData
import kotlinx.coroutines.flow.MutableSharedFlow

class LoginRepository(viewModel: ViewModel?) : BaseRepository(viewModel) {

    private val api : Main by single()

    fun login(username:String,password:String,data: MutableSharedFlow<KResult<JsonData<Auth>>>){
        androidScope {
            callResult {
                hold(data){
                    api.login(username, password)
                }
            }
        }
    }

    fun register(username:String,password:String,
                 data: MutableSharedFlow<KResult<JsonData<Empty>>>,
                 login: MutableSharedFlow<KResult<JsonData<Auth>>>,){
        androidScope {
            callResult {
                hold(data) {
                    api.register(username, password,password)
                }.success {
                        login(username,password,login)
                    }
            }
        }
    }


}