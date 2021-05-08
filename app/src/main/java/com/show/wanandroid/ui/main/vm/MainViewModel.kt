package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.show.kcore.http.coroutines.KResultData
import com.show.wanandroid.bean.*
import com.show.wanandroid.ui.main.repository.MainRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val repository by lazy { MainRepository(this) }

    val banner by lazy { KResultData<JsonData<List<Banner>>>() }
    val homeArticle by lazy { KResultData<JsonData<Article>>()  }
    val homeTops by lazy { KResultData<List<DatasBean>>() }
    val tabs by lazy { KResultData<JsonData<List<TabBeanItem>>>() }
    val cateTab by lazy { KResultData<JsonData<List<CateTab>>>() }

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


    fun getChaptersArticle(id:Int,page:Int,data: KResultData<JsonData<Article>>){
        repository.getChaptersArticle(id, page, data)
    }

    fun getCateTab(){
        repository.getCateTab(cateTab)
    }

    fun getCate(pager:Int,cid:Int,data: KResultData<JsonData<CateBean>>){
        repository.getCate(pager, cid, data)
    }


    fun homeCollect(id:Int) = repository.homeCollect(id)
    fun homeUnCollect(id:Int) = repository.homeUnCollect(id)

}