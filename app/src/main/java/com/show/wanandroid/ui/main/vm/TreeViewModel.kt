package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.show.kcore.http.coroutines.KResultData
import com.show.wanandroid.bean.Article
import com.show.wanandroid.bean.JsonData
import com.show.wanandroid.bean.Tree
import com.show.wanandroid.ui.main.repository.MainRepository

class TreeViewModel(application: Application) : AndroidViewModel(application) {

    val repository by lazy { MainRepository(this) }
    val navigator by lazy { MutableLiveData<Pair<Int,String>>() }

    val trees by lazy { KResultData<JsonData<List<Tree>>>() }


    fun getTree(){
        repository.getTree(trees)
    }

    fun getTreeArticle(id:Int,page:Int,data: KResultData<JsonData<Article>>){
        repository.getTreeArticle(id,page,data)
    }

}