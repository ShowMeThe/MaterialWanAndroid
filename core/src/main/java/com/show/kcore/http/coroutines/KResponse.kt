package com.show.kcore.http.coroutines

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.show.kcore.http.coroutines.KResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import java.lang.Exception

class KResponse<T>(val data: MutableSharedFlow<KResult<T>>? = null) : IResponse<T> {


    override suspend fun doOnLoading() {
        postData(LoadingResult.create())
        onLoading?.apply {
            withContext(Dispatchers.Main.immediate) {
                invoke()
            }
        }
    }

    override suspend fun doOnTimeOut() {
        val out = TimeOutResult.create<T>()
        postData(out)
        loadingTimeOut?.apply {
            withContext(Dispatchers.Main.immediate) {
                invoke(out)
            }
        }
    }


    override suspend fun doOnError(e: Throwable, t: T?) {
        val out = FailedResult.create<T>(Exception(e))
        postData(out)
        onError?.apply {
            withContext(Dispatchers.Main.immediate) {
                invoke(out)
            }
        }
    }

    override suspend fun doOnSuccess(t: T?) {
        val out = SuccessResult.create(t)
        postData(out)
        onSuccess?.apply {
            withContext(Dispatchers.Main.immediate) {
                invoke(out)
            }
        }
    }


    private var onLoading: (() -> Unit)? = null

    fun loading(onLoading: (() -> Unit)): KResponse<T> {
        this.onLoading = onLoading
        return this
    }

    private var loadingTimeOut: ((result: KResult<T>) -> Unit)? = null
    fun timeOut(loadingTimeOut: (KResult<T>.() -> Unit)): KResponse<T> {
        this.loadingTimeOut = loadingTimeOut
        return this
    }


    private var onSuccess: ((result: KResult<T>) -> Unit)? = null

    private var onError: ((result: KResult<T>) -> Unit)? = null

    fun success(onSuccess: (KResult<T>.() -> Unit)): KResponse<T> {
        this.onSuccess = onSuccess
        return this
    }


    fun error(onError: (KResult<T>.() -> Unit)): KResponse<T> {
        this.onError = onError
        return this
    }



    private suspend fun postData(call: KResult<T>): KResult<T> {
        data?.emit(call)
        return call
    }


}