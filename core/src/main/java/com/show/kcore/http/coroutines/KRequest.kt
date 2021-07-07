package com.show.kcore.http.coroutines

import android.util.Log
import com.show.kcore.extras.log.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.util.concurrent.TimeUnit

class KRequest<T>(private val request: suspend () -> Response<T>) {

    companion object {
        private val TAG = "Request"
    }

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


    suspend fun addAsync(
        scope: CoroutineScope,
        onError: (suspend Throwable.() -> Unit)?
    ): Flow<Response<T>?> {
        return flow {
            emit(tryRepeat(scope, onError))
        }
    }

    private suspend fun <T> Deferred<T>.awaitWithTimeout(time: Long): T? {
        return withTimeoutOrNull(time) { await() }
    }


    private suspend fun tryRepeat(
        scope: CoroutineScope,
        onError: (suspend Throwable.() -> Unit)?
    ): Response<T>? {
        return kotlin.runCatching {
            val out = scope.async {
                request.invoke()
            }.awaitWithTimeout(timeOut)
            Logger.dLog(TAG, "response = $out")
            if (out == null && tryCount < repeatTime) {
                tryCount++
                tryRepeat(scope, onError)
            } else {
                out
            }
        }.onFailure {
            it.printStackTrace()
            onError?.invoke(it)
        }.getOrNull()
    }

}