package com.show.kcore.http.coroutines

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.Response
import java.util.concurrent.TimeUnit

class KRequest<T>(private val request: suspend () -> Response<T>) {


    private var timeOut = 15000L
    private var repeatTime = 1
    private var tryCount = 0

    fun timeOut(time: Long): KRequest<T> {
        timeOut = time
        return this
    }

    fun repeatTime(repeat: Int): KRequest<T> {
        repeatTime = repeat
        return this
    }


    suspend fun addAsync(scope: CoroutineScope, onError:(suspend Throwable.() -> Unit)?): Response<T>? {
        val deferred = scope.async(Dispatchers.IO) {
            doRequest()
        }
        return tryRepeat(deferred, onError)
    }

    private suspend fun doRequest(): Response<T> {
        return request.invoke()
    }

    private suspend fun tryRepeat(
        deferred: Deferred<Response<T>>,
        onError:  (suspend Throwable.() -> Unit)?
    ): Response<T>? {
        return kotlin.runCatching {
            val out = withTimeoutOrNull(timeOut) {
                deferred.await()
            }
            Log.e("222","tryRepeat = $out")
            if (out == null && tryCount < repeatTime) {
                tryCount++
                tryRepeat(deferred, onError)
            } else {
                out
            }
        }.onFailure {
            it.printStackTrace()
            onError?.invoke(it)
        }.getOrNull()
    }

}