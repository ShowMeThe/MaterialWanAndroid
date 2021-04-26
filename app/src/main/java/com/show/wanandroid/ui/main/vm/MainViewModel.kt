package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.show.kcore.http.coroutines.KResultData
import com.show.wanandroid.bean.Banner
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.bean.JsonData
import com.show.wanandroid.ui.main.repository.MainRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val repository by lazy { MainRepository(this) }

    val banner by lazy { KResultData<JsonData<List<Banner>>>() }
    val homeTops by lazy { KResultData<List<DatasBean>>() }

    fun getBanner(){
        repository.getBanner(banner)
    }

    fun getHomeTop(){
        repository.getArticleTop(homeTops)
    }

}