package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.show.kcore.http.coroutines.KResult
import com.show.wanandroid.bean.Article
import com.show.wanandroid.bean.JsonData
import com.show.wanandroid.bean.Tree
import com.show.wanandroid.ui.main.repository.MainRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class TreeViewModel(application: Application) : AndroidViewModel(application) {

    val repository by lazy { MainRepository(this) }
    val navigator by lazy { MutableSharedFlow<Pair<Int,String>?>(replay = 1,onBufferOverflow = BufferOverflow.DROP_OLDEST) }

    val trees by lazy { MutableSharedFlow<KResult<JsonData<List<Tree>>>>() }


    fun getTree(){
        repository.getTree(trees)
    }

    fun getTreeArticle(id:Int,page:Int,data: MutableSharedFlow<KResult<JsonData<Article>>>){
        repository.getTreeArticle(id,page,data)
    }

}