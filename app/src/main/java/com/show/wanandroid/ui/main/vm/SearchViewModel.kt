package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.show.kcore.http.coroutines.KResult
import com.show.wanandroid.bean.Article
import com.show.wanandroid.bean.JsonData
import com.show.wanandroid.bean.KeyWord
import com.show.wanandroid.ui.main.repository.MainRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository by lazy { MainRepository(this) }
    val search = MutableSharedFlow<KResult<JsonData<Article>>>()
    val hotKey = MutableSharedFlow<KResult<JsonData<List<KeyWord>>>>()

    fun search(pager: Int,k:String) = repository.search(pager,k,search)


    fun getHotKey() = repository.getHotKey(hotKey)

}