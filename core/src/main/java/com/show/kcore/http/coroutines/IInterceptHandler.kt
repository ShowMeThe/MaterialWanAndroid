package com.show.kcore.http.coroutines

interface IInterceptHandler {

    suspend fun onIntercept(maxTimeOut:Long,repeatTime:Int,retryCount: Int): InterceptState

}

enum class InterceptState{
    ForceIntercept,ContinueState
}