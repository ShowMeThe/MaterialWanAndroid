package com.show.wanandroid.ui.main.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.show.kInject.core.ext.single
import com.show.kcore.base.BaseRepository
import com.show.kcore.http.coroutines.IFunction
import com.show.kcore.http.coroutines.KResult
import com.show.kcore.http.coroutines.KResultData
import com.show.kcore.http.coroutines.callResult
import com.show.wanandroid.api.Main
import com.show.wanandroid.bean.*
import java.lang.Exception

class MainRepository(viewModel: ViewModel?) : BaseRepository(viewModel) {

    private val api: Main by single()

    fun getBanner(data: KResultData<JsonData<List<Banner>>>) {
        androidScope {
            callResult {
                hold { api.banner() }
                    .bindData(data)
            }
        }
    }
    fun getHomeArticle(page: Int,liveData: KResultData<JsonData<Article>>){
        androidScope {
            callResult {
                hold { api.getHomeArticle(page) }
                    .bindData(liveData)
            }
        }
    }

    fun getArticleTop(liveData: KResultData<List<DatasBean>>) {
        androidScope {
            callResult {
                merge({ api.getHomeArticle(0) },
                    { api.getHomeTop() },
                    object :
                        IFunction<JsonData<Article>, JsonData<List<DatasBean>>, List<DatasBean>> {
                        override fun apply(
                            t1: JsonData<Article>?,
                            t2: JsonData<List<DatasBean>>?
                        ): List<DatasBean> {
                            val list = ArrayList<DatasBean>()
                            t2?.apply {
                                if (isLegal() && data != null) {
                                    data!!.forEach {
                                        it.top = true
                                    }
                                    list.addAll(data!!)
                                }
                            }
                            t1?.apply {
                                if (isLegal() && data != null) {
                                    list.addAll(data!!.datas)
                                }
                            }
                            return list
                        }

                    }).bindData(liveData)
            }
        }
    }


    fun getChapters(data: KResultData<JsonData<List<TabBeanItem>>>) {
        androidScope {
            callResult {
                hold { api.getChapters() }
                    .bindData(data)
            }
        }
    }

    fun getChaptersArticle(id:Int,page:Int,data: KResultData<JsonData<Article>>) {
        androidScope {
            callResult {
                hold { api.getArticle(id, page) }
                    .bindData(data)
            }
        }
    }

    fun getTree(data: KResultData<JsonData<List<Tree>>>) {
        androidScope {
            callResult {
                hold { api.getTree() }
                    .bindData(data)
            }
        }
    }

    fun getTreeArticle(id:Int,page:Int,data: KResultData<JsonData<Article>>) {
        androidScope {
            callResult {
                hold { api.getTreeArticle(page,id) }
                    .bindData(data)
            }
        }
    }


    fun getCateTab(data: KResultData<JsonData<List<CateTab>>>) {
        androidScope {
            callResult {
                hold { api.getCateTab() }
                    .bindData(data)
            }
        }
    }


    fun getCate(pager:Int,cid:Int,data: KResultData<JsonData<CateBean>>) {
        androidScope {
            callResult {
                hold { api.getCate(pager,cid) }
                    .bindData(data)
            }
        }
    }

}