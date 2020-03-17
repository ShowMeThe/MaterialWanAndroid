package com.show.wanandroid.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ken.materialwanandroid.entity.Empty
import com.show.wanandroid.api.Main
import com.show.wanandroid.entity.Article
import com.show.wanandroid.entity.Banner
import com.show.wanandroid.entity.TabBean
import com.show.wanandroid.entity.TabItem
import com.show.wanandroid.toast
import showmethe.github.core.base.BaseRepository
import showmethe.github.core.http.coroutines.CallResult
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.kinit.inject

class MainRepository  : BaseRepository() {

    private val api: Main by inject()


    fun getBanner(call: MutableLiveData<Result<ArrayList<Banner>>>) {
        CallResult<ArrayList<Banner>>(owner) {
            post(call)
            hold {
                api.banner()
            }
        }
    }


    fun getChapters(call: MutableLiveData<Result<ArrayList<TabBean>>>) {
        CallResult<ArrayList<TabBean>>(owner){
            post(call)
            hold {
                api.getChapters()
            }
        }
    }

    fun getArticle(id: Int, pager: Int, call: MutableLiveData<Result<Article>>) {
        CallResult<Article>(owner) {
            post(call)
            hold {
                api.getArticle(id, pager)
            }
        }
    }


    fun getHomeArticle(pager: Int, call: MutableLiveData<Result<Article>>) {
        CallResult<Article>(owner) {
            post(call)
            hold {
                api.getHomeArticle(pager)
            }
        }
    }


    fun homeCollect(id: Int) {
        CallResult<Empty>(owner) {
            success { result, message ->
                showToast("收藏成功")
            }
            error { result, code, message ->
                toast(code, message)
            }
            hold {
                api.homeCollect(id)
            }
        }
    }

    fun homeUnCollect(id: Int) {
        CallResult<Empty>(owner) {
            success { result, message ->
                showToast("取消收藏")
            }.error { result, code, message ->
                toast(code, message)
            }.hold {
                api.homeUnCollect(id)
            }

        }
    }

}