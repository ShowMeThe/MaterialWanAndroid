package com.show.kcore.http.coroutines

import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.lang.Exception

@Keep
sealed class KResult<T> {

    companion object {

        const val Loading = "Loading"
        const val Success = "Success"
        const val Failure = "Failure"
        const val TimeOut = "TimeOut"
    }

    lateinit var status: String
    var response: T? = null
    var exception: Exception? = null
}

@Keep
class SuccessResult<T> : KResult<T>() {

    companion object {

        fun <T> create(response: T?): SuccessResult<T> {
            return SuccessResult<T>().apply {
                status = Loading
                this.response = response
            }
        }
    }
}

@Keep
class FailedResult<T> : KResult<T>() {
    companion object {
        fun <T> create(exception: Exception?, t: T? = null): FailedResult<T> {
            return FailedResult<T>().apply {
                status = Failure
                this.response = t
                this.exception = exception
            }
        }
    }
}

@Keep
class TimeOutResult<T> : KResult<T>() {
    companion object {
        fun <T> create(): TimeOutResult<T> {
            return TimeOutResult<T>().apply {
                status = TimeOut
            }
        }
    }
}

@Keep
class LoadingResult<T> : KResult<T>() {
    companion object {
        fun <T> create(): LoadingResult<T> {
            return LoadingResult<T>().apply {
                status = Loading
            }
        }
    }
}


fun <T> kResultFlow(
    replay: Int = 0,
    extraBufferCapacity: Int = 0,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
) = MutableSharedFlow<T>(replay, extraBufferCapacity, onBufferOverflow)