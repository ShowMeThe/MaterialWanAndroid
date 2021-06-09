package com.show.kcore.http.coroutines

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.show.kcore.extras.log.Logger
import kotlinx.coroutines.*
import retrofit2.Response
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


fun LifecycleOwner.androidScope(scope: LifecycleOwner.() -> Unit) {
    scope(this)
}

fun LifecycleOwner?.callResult(scope: CallResult.() -> Unit) {
    CallResult(this, scope)
}

fun callResult(scope: CallResult.() -> Unit) {
    CallResult(null, scope)
}

class CallResult constructor(
    private var owner: LifecycleOwner?,
    var callResult: (CallResult.() -> Unit)?
) {


    private val TAG = "CallResult"

    private var timeOut = 15000L
    private var repeatTime = 1


    init {
        callResult?.invoke(this)
    }


    fun timeOut(time: Long, util: TimeUnit) {
        timeOut = util.toMillis(time)
    }

    fun repeatTime(repeat: Int) {
        repeatTime = repeat
    }


    fun <T1, T2, R> merge(
        data: KResultData<R>? = null,
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
        if (owner == null || owner?.lifecycle?.currentState == Lifecycle.State.INITIALIZED) {
            GlobalScope.launch(Dispatchers.IO) {
                merge(kResponse,iFunction,kRequest1, onError1, kRequest2, onError2)
            }
        } else {
            owner?.lifecycleScope?.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    merge(kResponse,iFunction,kRequest1, onError1, kRequest2, onError2)
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
                .addAsync(this, onError1),
            response2 = kRequest2
                .timeOut(timeOut)
                .repeatTime(repeatTime)
                .addAsync(this, onError2), iFunction, kResponse
        )
    }


    fun <T> hold(
        data: MutableLiveData<KResult<T>>? = null,
        request: suspend () -> Response<T>
    ): KResponse<T> {
        val kResponse = KResponse<T>(data)
        val kRequest = KRequest(request)
        if (owner == null || owner?.lifecycle?.currentState == Lifecycle.State.INITIALIZED) {
            GlobalScope.launch(Dispatchers.IO) {
                single(kResponse, kRequest)
            }
        } else {
            owner?.lifecycleScope?.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    single(kResponse, kRequest)
                }
            }
        }
        return kResponse
    }

    private suspend fun <T> CoroutineScope.single(kResponse: KResponse<T>, kRequest: KRequest<T>) {
        kResponse.doOnLoading()
        singleResult(kRequest
            .timeOut(timeOut)
            .repeatTime(repeatTime)
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