package com.show.wanandroid.ui.main.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.show.wanandroid.entity.Article
import com.show.wanandroid.entity.KeyWord
import com.show.wanandroid.ui.main.repository.MainRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath
import showmethe.github.core.http.coroutines.Result

class SearchViewModel(application: Application) : BaseViewModel(application) {


    @InjectOwner
    val repository = MainRepository()

    val searchWord = MutableLiveData<String>()
    val search = MutableLiveData<Result<Article>>()
    val hotKey = MutableLiveData<Result<ArrayList<KeyWord>>>()

    override fun onViewModelCreated(owner: LifecycleOwner) {


    }


    /**
     * search
     */
    @VMPath(path = "getHotKey")
    fun getHotKey() = repository.getHotKey(hotKey)
    @VMPath(path = "search")
    fun search(pager: Int,k:String) = repository.search(pager,k,search)


}