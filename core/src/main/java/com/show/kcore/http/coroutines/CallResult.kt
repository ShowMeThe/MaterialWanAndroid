package com.show.kcore.http.coroutines

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import java.util.concurrent.TimeUnit
import java.util.logging.Logger


fun LifecycleOwner.androidScope(scope: LifecycleOwner.() -> Unit) {
    scope(this)
}

fun  LifecycleOwner?.callResult(scope: CallResult.() -> Unit) {
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

    init {
        callResult?.invoke(this)
    }


    private var timeOut = 15000L
    private var repeatTime = 0

    fun timeOut(time: Long, util: TimeUnit) {
        timeOut = util.toMillis(time)
    }

    fun repeatTime(repeat: Int) {
        repeatTime = repeat
    }


    fun <T1, T2, R> merge(
        request1: suspend () -> Response<T1>,
        request2: suspend () -> Response<T2>,
        iFunction: IFunction<T1, T2, R>
    ): KResponse<R> {
        val kResponse = KResponse<R>()
        val kRequest1 = KRequest(request1)
        val kRequest2 = KRequest(request2)
        val onError1 : suspend ( e: Throwable)-> Unit = { e ->
            if(e is TimeoutCancellationException){
                e.printStackTrace()
                kResponse.doOnTimeOut()
            }else{
                kResponse.doOnError(e,null)
            }
        }
        val onError2 : suspend (e:Throwable)-> Unit = { e ->
            if(e is TimeoutCancellationException){
                e.printStackTrace()
                kResponse.doOnTimeOut()
            }else{
                kResponse.doOnError(e,null)
            }
        }
        if (owner == null) {
            GlobalScope.launch(Dispatchers.IO) {
                kResponse.doOnLoading()
                mergeResult(
                    response = kRequest1
                        .timeOut(timeOut)
                        .repeatTime(repeatTime)
                        .addAsync(this,onError1),

                    response2 = kRequest2
                        .timeOut(timeOut)
                        .repeatTime(repeatTime)
                        .addAsync(this,onError2), iFunction,kResponse)
            }
        }else{
            owner?.lifecycleScope?.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    kResponse.doOnLoading()
                    mergeResult(
                        response = kRequest1
                        .timeOut(timeOut)
                        .repeatTime(repeatTime)
                        .addAsync(this,onError1),
                        response2 = kRequest2
                            .timeOut(timeOut)
                            .repeatTime(repeatTime)
                            .addAsync(this,onError2), iFunction,kResponse)
                }
            }
        }
        return kResponse
    }


    fun <T> hold(request: suspend () -> Response<T>): KResponse<T> {
        val kResponse = KResponse<T>()
        val kRequest = KRequest(request)
        if (owner == null) {
            GlobalScope.launch(Dispatchers.IO) {
                kResponse.doOnLoading()
                singleResult(kRequest
                    .timeOut(timeOut)
                    .repeatTime(repeatTime)
                    .addAsync(this){
                        if(this is TimeoutCancellationException){
                            this.printStackTrace()
                            kResponse.doOnTimeOut()
                        }else{
                            kResponse.doOnError(this,null)
                        }
                    }, kResponse)
            }
        } else {
            owner?.lifecycleScope?.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    kResponse.doOnLoading()
                    singleResult(kRequest
                        .timeOut(timeOut)
                        .repeatTime(repeatTime)
                        .addAsync(this) {
                            this.printStackTrace()
                           if(this is TimeoutCancellationException){
                               kResponse.doOnTimeOut()
                           }else{
                               kResponse.doOnError(this,null)
                           }
                        }, kResponse)

                }
            }
        }
        return kResponse
    }

}