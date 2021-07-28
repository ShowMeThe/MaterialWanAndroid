package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.show.kcore.http.coroutines.KResponse
import com.show.kcore.http.coroutines.KResult
import com.show.wanandroid.bean.*
import com.show.wanandroid.ui.main.repository.MainRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val repository by lazy { MainRepository(this) }

    val banner by lazy { MutableSharedFlow<KResult<JsonData<List<Banner>>>>() }
    val homeArticle by lazy { MutableSharedFlow<KResult<JsonData<Article>>>()  }
    val homeTops by lazy { MutableSharedFlow<KResult<List<DatasBean>>>() }
    val tabs by lazy { MutableSharedFlow<KResult<JsonData<List<TabBeanItem>>>>() }
    val cateTab by lazy { MutableSharedFlow<KResult<JsonData<List<CateTab>>>>() }
    val collects by lazy { MutableSharedFlow<KResult<JsonData<Collect>>>() }

    fun getBanner(){
        repository.getBanner(banner)
    }

    fun getHomeTop(){
        repository.getArticleTop(homeTops)
    }

    fun getHomeArticle(page: Int){
        repository.getHomeArticle(page,homeArticle)
    }


    fun getChapters(){
        repository.getChapters(tabs)
    }


    fun getChaptersArticle(id:Int,page:Int,data: MutableSharedFlow<KResult<JsonData<Article>>>){
        repository.getChaptersArticle(id, page, data)
    }

    fun getCateTab(){
        repository.getCateTab(cateTab)
    }

    fun getCate(pager:Int,cid:Int,data: MutableSharedFlow<KResult<JsonData<CateBean>>>){
        repository.getCate(pager, cid, data)
    }

    fun getCollects(page: Int){
        repository.getCollects(page,collects)
    }

    fun homeCollect(id:Int) = repository.homeCollect(id)
    fun homeUnCollect(id:Int) = repository.homeUnCollect(id)
    fun unCollect(id:Int,originId: Int) = repository.unCollect(id,originId)


}