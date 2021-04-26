package com.show.wanandroid.bean

import com.show.kcore.http.JsonResult

class JsonData<T> : JsonResult {
    var errorCode: Int = -1
    var errorMsg: String? = null
    var data :T? = null
    override fun isLegal(): Boolean {
        return errorCode == 0
    }
}