package com.show.kcore.http.coroutines

import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.MutableLiveData
import java.lang.Exception

@Keep
data class KResult<T>(var status:String,
                 var response: T? = null,
                 var exception: Exception? = null
){

    companion object{

        const val Loading = "Loading"
        const val Success = "Success"
        const val Failure = "Failure"
        const val TimeOut = "TimeOut"

    }

}


typealias KResultData<T> = MutableLiveData<KResult<T>>