package com.show.kcore.http.coroutines

import android.util.Log
import com.show.kcore.http.JsonResult
import retrofit2.Response


internal suspend fun <T1, T2, R> mergeResult(
    response: Response<T1>?,
    response2: Response<T2>?,
    iFunction: IFunction<T1, T2, R>,
    iResponse: IResponse<R>
) {
    runCatching {
        if (response == null || response2 == null) {
            iResponse.doOnError(Exception("Response is null"))
            return
        }
        if(response.code() != 200 || response2.code() != 200){
            iResponse.doOnTimeOut()
            return
        }
        val result = iFunction.apply(response.body(), response2.body())
        iResponse.doOnSuccess(result)
    }.getOrElse {
        iResponse.doOnError(it)
    }
}

internal suspend fun <T> singleResult(
    response: Response<T>?, iResponse: IResponse<T>
) {
    runCatching {
        if (response == null) {
            return
        }
        response.apply {
            if (code() == 200) {
                if (body() != null) {
                    if (body() is JsonResult) {
                        val jsonResult = (body() as JsonResult)
                        if (jsonResult.isLegal()) {
                            iResponse.doOnSuccess(body())
                        } else {
                            iResponse.doOnError(Exception("JsonResult is illegal"))
                        }
                    } else {
                        iResponse.doOnSuccess(body())
                    }
                } else {
                    iResponse.doOnSuccess(body())
                }
            } else {
                iResponse.doOnError(Exception(response.toString()))
            }
        }
    }.getOrElse {
        iResponse.doOnError(it)
    }
}


