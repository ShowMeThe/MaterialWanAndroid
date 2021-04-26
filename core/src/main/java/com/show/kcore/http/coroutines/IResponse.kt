package com.show.kcore.http.coroutines

interface IResponse<T> {

    suspend fun doOnLoading()

    suspend fun doOnTimeOut()

    suspend fun doOnError(e:Throwable)

    suspend fun doOnSuccess(t: T?)

}