package com.show.kcore.http.coroutines

import android.util.Log
import com.show.kcore.http.JsonResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import retrofit2.Response


internal suspend fun <T1, T2, R> mergeResult(
    response: Flow<Response<T1>?>,
    response2: Flow<Response<T2>?>,
    iFunction: IFunction<T1, T2, R>,
    iResponse: IResponse<R>
) {
    runCatching {
        response.combine(response2) { t1, t2 ->
            return@combine if (t1 == null && t2 == null) {
                iResponse.doOnError(Exception("Response is null"), null)
                null
            } else if (t1?.code() != 200 && t2?.code() != 200) {
                iResponse.doOnTimeOut()
                null
            } else {
                iFunction.apply(t1?.body(), t2?.body())
            }
        }.collect { result ->
            if (result != null) {
                iResponse.doOnSuccess(result)
            }
        }
    }.getOrElse {
        iResponse.doOnError(it, null)
    }
}

internal suspend fun <T> singleResult(
    response: Flow<Response<T>?>, iResponse: IResponse<T>
) {
    runCatching {
        response.collect { t ->
            Log.e("2222222","singleResult $t")
            if (t == null) {
                Log.e("2222222","iResponse $t")
                iResponse.doOnError(Exception("response is null"), null)
            } else {
                t.apply {
                    if (code() == 200) {
                        if (body() != null) {
                            if (body() is JsonResult) {
                                val jsonResult = (body() as JsonResult)
                                if (jsonResult.isLegal()) {
                                    iResponse.doOnSuccess(body())
                                } else {
                                    iResponse.doOnError(Exception("JsonResult is illegal"), body())
                                }
                            } else {
                                iResponse.doOnSuccess(body())
                            }
                        } else {
                            iResponse.doOnSuccess(body())
                        }
                    } else {
                        iResponse.doOnError(Exception(response.toString()), body())
                    }
                }
            }
        }
    }.getOrElse {
        iResponse.doOnError(it, null)
    }
}


