package com.show.kcore.http.coroutines

import android.util.ArrayMap
import android.util.Log
import androidx.lifecycle.*
import com.show.kcore.extras.log.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


fun Coroutines.callResult(scope: CallResult.() -> Unit) {
    CallResult(this, scope)
}

data class Coroutines(
    val viewModelScope: CoroutineScope? = null,
    val owner: LifecycleOwner? = null
)

class CallResult constructor(
    private var coroutines: Coroutines,
    var callResult: (CallResult.() -> Unit)?
) {


    private val TAG = "CallResult"

    private var timeOut = 15000L
    private var repeatTime = 1

    private val intercepts by lazy { ArrayMap<String, IInterceptHandler>() }

    private val REQUEST_TAG_1 = "REQUEST_TAG_1"
    private val REQUEST_TAG_2 = "REQUEST_TAG_2"

    init {
        callResult?.invoke(this)
    }


    fun addInterceptForRequest(intercept: IInterceptHandler) {
        intercepts[REQUEST_TAG_1] = intercept
    }

    fun addInterceptForRequest2(intercept: IInterceptHandler) {
        intercepts[REQUEST_TAG_2] = intercept
    }

    fun timeOut(time: Long, util: TimeUnit) {
        timeOut = util.toMillis(time)
    }

    fun repeatTime(repeat: Int) {
        repeatTime = repeat
    }


    fun <T1, T2, R> merge(
        data: MutableSharedFlow<KResult<R>>? = null,
        request1: suspend () -> Response<T1>,
        request2: suspend () -> Response<T2>,
        iFunction: IFunction<T1, T2, R>
    ): KResponse<R> {
        val kResponse = KResponse<R>(data)
        val kRequest1 = KRequest(request1)
        val kRequest2 = KRequest(request2)
        val onError1: suspend (e: Throwable) -> Unit = { e ->
            if (e is TimeoutCancellationException) {
                e.printStackTrace()
                kResponse.doOnTimeOut()
            } else {
                kResponse.doOnError(e, null)
            }
        }
        val onError2: suspend (e: Throwable) -> Unit = { e ->
            if (e is TimeoutCancellationException) {
                e.printStackTrace()
                kResponse.doOnTimeOut()
            } else {
                kResponse.doOnError(e, null)
            }
        }
        val owner = coroutines.owner
        val viewModelScope = coroutines.viewModelScope
        if (owner == null || owner.lifecycle.currentState == Lifecycle.State.INITIALIZED) {
            viewModelScope?.launch(Dispatchers.IO) {
                merge(kResponse, iFunction, kRequest1, onError1, kRequest2, onError2)
            }
        } else {
            owner.lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    merge(kResponse, iFunction, kRequest1, onError1, kRequest2, onError2)
                }
            }
        }
        return kResponse
    }


    private suspend fun <T1, T2, R> CoroutineScope.merge(
        kResponse: KResponse<R>,
        iFunction: IFunction<T1, T2, R>,
        kRequest1: KRequest<T1>, onError1: suspend (e: Throwable) -> Unit,
        kRequest2: KRequest<T2>, onError2: suspend (e: Throwable) -> Unit
    ) {
        kResponse.doOnLoading()
        mergeResult(
            response = kRequest1
                .timeOut(timeOut)
                .repeatTime(repeatTime)
                .setIntercept(intercepts[REQUEST_TAG_1])
                .addAsync(this, onError1),
            response2 = kRequest2
                .timeOut(timeOut)
                .repeatTime(repeatTime)
                .setIntercept(intercepts[REQUEST_TAG_2])
                .addAsync(this, onError2), iFunction, kResponse
        )
    }


    fun <T> hold(
        data: MutableSharedFlow<KResult<T>>? = null,
        request: suspend () -> Response<T>
    ): KResponse<T> {
        val kResponse = KResponse<T>(data)
        val kRequest = KRequest(request)
        val owner = coroutines.owner
        val viewModelScope = coroutines.viewModelScope
        if (owner == null || owner.lifecycle.currentState == Lifecycle.State.INITIALIZED) {
            viewModelScope?.launch(Dispatchers.IO) {
                single(kResponse, kRequest)
            }
        } else {
            owner.lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    single(kResponse, kRequest)
                }
            }
        }
        return kResponse
    }

    private suspend fun <T> CoroutineScope.single(kResponse: KResponse<T>, kRequest: KRequest<T>) {
        kResponse.doOnLoading()
        singleResult(
            kRequest
                .timeOut(timeOut)
                .repeatTime(repeatTime)
                .setIntercept(intercepts[REQUEST_TAG_1])
                .addAsync(this) {
                    this.printStackTrace()
                    if (this is TimeoutCancellationException) {
                        kResponse.doOnTimeOut()
                    } else {
                        kResponse.doOnError(this, null)
                    }
                }, kResponse
        )
    }


}