package com.show.wanandroid.ui.main.vm

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.show.wanandroid.entity.*
import com.show.wanandroid.ui.main.MainRepository
import showmethe.github.core.base.BaseViewModel
import showmethe.github.core.base.InjectOwner
import showmethe.github.core.base.vmpath.VMPath
import showmethe.github.core.http.coroutines.Result

class MainViewModel(application: Application) : BaseViewModel(application) {

    @InjectOwner
    val repository = MainRepository()

    val banner = MutableLiveData<Result<ArrayList<Banner>>>()
    val tops = MutableLiveData<Result<ArrayList<Article.DatasBean>>>()
    val article = MutableLiveData<Result<Article>>()
    val tabs = MutableLiveData<Result<ArrayList<TabBean>>>()
    val tree = MutableLiveData<Result<ArrayList<Tree>>>()

    val treeNavigator = MutableLiveData<Pair<Int,String>>()
    val treeNavBack  = MutableLiveData<Boolean>()

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
    @VMPath(path = "getHomeTop")
    fun getHomeTop() = repository.getHomeTopArticle(tops)

    /**
     * Chapters
     */
    @VMPath(path = "getChapters")
    fun getChapters() = repository.getChapters(tabs)
    @VMPath(path = "getArticle")
    fun getArticle(id:Int,pager:Int,article:MutableLiveData<Result<Article>>) = repository.getArticle(id,pager,article)


    /**
     * Tree
     */
    @VMPath(path = "getTree")
    fun getTree() = repository.getTree(tree)
    @VMPath(path = "getTreeArticle")
    fun getTreeArticle(pager: Int,id: Int,call:MutableLiveData<Result<Article>>) = repository.getTreeArticle(pager,id,call)

    /**
     * Collect
     */
    @VMPath("homeCollect")
    fun homeCollect(id:Int) = repository.homeCollect(id)
    @VMPath("homeUnCollect")
    fun homeUnCollect(id:Int) = repository.homeUnCollect(id)
}