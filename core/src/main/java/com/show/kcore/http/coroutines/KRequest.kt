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

    private var intercept: IInterceptHandler? = null
    private var timeOut = 15000L
    private var repeatTime = 1
    private var tryCount = 0

    fun setIntercept(intercept: IInterceptHandler?): KRequest<T> {
        this.intercept = intercept
        return this
    }

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
                val state = intercept?.onIntercept(timeOut, repeatTime, tryCount)
                    ?: InterceptState.ContinueState
                Logger.dLog(TAG, "tryRepeat state = $state intercept = $intercept")
                if (state == InterceptState.ContinueState) {
                    RequestResult(InterceptState.ContinueState, request.invoke())
                } else {
                    RequestResult(state, null)
                }
            }.awaitWithTimeout(timeOut)
            Logger.dLog(TAG, "tryRepeat response body = ${out?.response?.body()}")
            if (out?.state != InterceptState.ForceIntercept && out?.response == null && tryCount < repeatTime) {
                tryCount++
                Logger.dLog(TAG, "tryRepeat $request in Loop")
                tryRepeat(scope, onError)
            } else {
                out?.response
            }
        }.onFailure {
            it.printStackTrace()
            onError?.invoke(it)
        }.getOrNull()
    }

    data class RequestResult<T>(val state: InterceptState, val response: Response<T>?)

}