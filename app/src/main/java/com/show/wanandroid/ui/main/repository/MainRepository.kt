package com.show.wanandroid.ui.main.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.show.kInject.core.ext.single
import com.show.kcore.base.BaseRepository
import com.show.kcore.http.coroutines.IFunction
import com.show.kcore.http.coroutines.KResult
import com.show.kcore.http.coroutines.callResult
import com.show.wanandroid.R
import com.show.wanandroid.api.Main
import com.show.wanandroid.bean.*
import com.show.wanandroid.toast
import com.squareup.moshi.Json
import java.lang.Exception

class MainRepository(viewModel: ViewModel?) : BaseRepository(viewModel) {

    private val api: Main by single()

    fun getBanner(data: MutableLiveData<KResult<JsonData<List<Banner>>>>) {
        androidScope {
            callResult {
                hold(data) { api.banner() }
            }
        }
    }

    fun getHomeArticle(page: Int, liveData: MutableLiveData<KResult<JsonData<Article>>>) {
        androidScope {
            callResult {
                hold(liveData) { api.getHomeArticle(page) }

            }
        }
    }

    fun getArticleTop(liveData: MutableLiveData<KResult<List<DatasBean>>>) {
        androidScope {
            callResult {
                merge(liveData, { api.getHomeArticle(0) },
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

                    })
            }
        }
    }


    fun getChapters(data: MutableLiveData<KResult<JsonData<List<TabBeanItem>>>>) {
        androidScope {
            callResult {
                hold(data) { api.getChapters() }

            }
        }
    }

    fun getChaptersArticle(id: Int, page: Int, data: MutableLiveData<KResult<JsonData<Article>>>) {
        androidScope {
            callResult {
                hold(data) { api.getArticle(id, page) }

            }
        }
    }

    fun getTree(data: MutableLiveData<KResult<JsonData<List<Tree>>>>) {
        androidScope {
            callResult {
                hold(data) { api.getTree() }
            }
        }
    }

    fun getTreeArticle(id: Int, page: Int, data: MutableLiveData<KResult<JsonData<Article>>>) {
        androidScope {
            callResult {
                hold(data) { api.getTreeArticle(page, id) }

            }
        }
    }


    fun getCateTab(data: MutableLiveData<KResult<JsonData<List<CateTab>>>>) {
        androidScope {
            callResult {
                hold(data) { api.getCateTab() }

            }
        }
    }


    fun getCate(pager: Int, cid: Int, data: MutableLiveData<KResult<JsonData<CateBean>>>) {
        androidScope {
            callResult {
                hold(data) { api.getCate(pager, cid) }
                    .success {
                        this.response?.data?.datas?.apply {
                        }
                    }
            }
        }
    }

    fun homeCollect(id: Int) {
        androidScope {
            callResult {
                hold { api.homeCollect(id) }
                    .success {
                        toast(0, R.string.success_collect)
                    }.error {
                        response?.apply {
                            toast(errorCode, errorMsg)
                        }
                    }
            }
        }
    }

    fun homeUnCollect(id: Int) {
        androidScope {
            callResult {
                hold { api.homeUnCollect(id) }
                    .success {
                        toast(0, R.string.cancel_collect)
                    }.error {
                        response?.apply {
                            toast(errorCode, errorMsg)
                        }
                    }
            }
        }
    }

    fun search(pager: Int, k: String, data: MutableLiveData<KResult<JsonData<Article>>>) {
        androidScope {
            callResult {
                hold(data) {
                    api.search(pager, k)
                }
            }
        }
    }

    fun getHotKey(data: MutableLiveData<KResult<JsonData<List<KeyWord>>>>) {
        androidScope {
            callResult {
                hold(data) {
                    api.getHotKey()
                }
            }
        }
    }

    fun getCollects(pager: Int, data: MutableLiveData<KResult<JsonData<Collect>>>) {
        androidScope {
            callResult {
                hold(data) {
                    api.getCollect(pager)
                }
            }
        }
    }

    fun unCollect(id: Int, originId: Int) {
        androidScope {
            callResult {
                hold {
                    api.unCollect(id, originId)
                }
            }
        }
    }

}