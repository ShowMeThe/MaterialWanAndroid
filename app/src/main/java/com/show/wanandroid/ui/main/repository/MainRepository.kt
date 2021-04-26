package com.show.wanandroid.ui.main.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.show.kInject.core.ext.single
import com.show.kcore.base.BaseRepository
import com.show.kcore.http.coroutines.IFunction
import com.show.kcore.http.coroutines.KResult
import com.show.kcore.http.coroutines.KResultData
import com.show.kcore.http.coroutines.callResult
import com.show.wanandroid.api.Main
import com.show.wanandroid.bean.Article
import com.show.wanandroid.bean.Banner
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.bean.JsonData

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


}