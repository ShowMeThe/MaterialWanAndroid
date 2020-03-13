package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.show.wanandroid.entity.Article
import com.show.wanandroid.entity.Banner
import com.show.wanandroid.ui.main.MainRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath
import showmethe.github.core.http.coroutines.Result

class MainViewModel(application: Application) : BaseViewModel(application) {

    @InjectOwner
    val repository = MainRepository()
    val banner = MutableLiveData<Result<ArrayList<Banner>>>()
    val article = MutableLiveData<Result<Article>>()

    override fun onViewModelCreated(owner: LifecycleOwner) {


    }



    /**
     * Banner
     */
    @VMPath(path = "getBanner")
    fun getBanner() = repository.getBanner(banner)


    /**
     * Article
     */
    @VMPath(path = "getHomeArticle")
    fun getHomeArticle(pager:Int) = repository.getHomeArticle(pager,article)




    /**
     * Collect
     */
    fun homeCollect(id:Int) = repository.homeCollect(id)

    fun homeUnCollect(id:Int) = repository.homeUnCollect(id)
}