package com.show.wanandroid.ui.main

import androidx.lifecycle.MutableLiveData
import com.show.wanandroid.api.Main
import com.show.wanandroid.entity.Banner
import showmethe.github.core.base.BaseRepository
import showmethe.github.core.http.coroutines.CallResult
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.kinit.inject

class MainRepository  : BaseRepository() {

    private val api: Main by inject()

    fun getBanner(call: MutableLiveData<Result<ArrayList<Banner>>>) {
        CallResult<ArrayList<Banner>>(owner) {
            post(call)
            hold {
                api.banner()
            }
        }
    }

}