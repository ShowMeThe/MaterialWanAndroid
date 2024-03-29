package com.show.wanandroid.ui.main.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.show.kInject.core.ext.single
import com.show.kcore.base.BaseRepository
import com.show.kcore.extras.log.Logger
import com.show.kcore.http.coroutines.IFunction
import com.show.kcore.http.coroutines.KResult
import com.show.kcore.http.coroutines.callResult
import com.show.wanandroid.R
import com.show.wanandroid.api.Main
import com.show.wanandroid.bean.*
import com.show.wanandroid.toast
import com.squareup.moshi.Json
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.Exception

class MainRepository(viewModel: ViewModel?) : BaseRepository(viewModel) {

    private val api: Main by single()

    private val intercept by lazy { SessionIntercept() }

    fun getBanner(data: MutableSharedFlow<KResult<JsonData<List<Banner>>>>) {
        androidScope {
            callResult {
                hold(data) { api.banner() }
            }
        }
    }

    fun getHomeArticle(page: Int, liveData: MutableSharedFlow<KResult<JsonData<Article>>>) {
        androidScope {
            callResult {
                hold(liveData) { api.getHomeArticle(page) }

            }
        }
    }

    fun getArticleTop(liveData: MutableSharedFlow<KResult<List<DatasBean>>>) {
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


    fun getChapters(data: MutableSharedFlow<KResult<JsonData<List<TabBeanItem>>>>) {
        androidScope {
            callResult {
                hold(data) { api.getChapters() }

            }
        }
    }

    fun getChaptersArticle(id: Int, page: Int, data: MutableSharedFlow<KResult<JsonData<Article>>>) {
        androidScope {
            callResult {
                hold(data) { api.getArticle(id, page) }

            }
        }
    }

    fun getTree(data: MutableSharedFlow<KResult<JsonData<List<Tree>>>>) {
        androidScope {
            callResult {
                hold(data) { api.getTree() }
            }
        }
    }

    fun getTreeArticle(id: Int, page: Int, data: MutableSharedFlow<KResult<JsonData<Article>>>) {
        androidScope {
            callResult {
                hold(data) { api.getTreeArticle(page, id) }

            }
        }
    }


    fun getCateTab(data: MutableSharedFlow<KResult<JsonData<List<CateTab>>>>) {
        androidScope {
            callResult {
                hold(data) { api.getCateTab() }

            }
        }
    }


    fun getCate(pager: Int, cid: Int, data: MutableSharedFlow<KResult<JsonData<CateBean>>>) {
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

    fun search(pager: Int, k: String, data: MutableSharedFlow<KResult<JsonData<Article>>>) {
        androidScope {
            callResult {
                hold(data) {
                    api.search(pager, k)
                }
            }
        }
    }

    fun getHotKey(data: MutableSharedFlow<KResult<JsonData<List<KeyWord>>>>) {
        androidScope {
            callResult {
                hold(data) {
                    api.getHotKey()
                }
            }
        }
    }

    fun getCollects(pager: Int, data: MutableSharedFlow<KResult<JsonData<Collect>>>) {
        androidScope {
            callResult {
                addInterceptForRequest(intercept)
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