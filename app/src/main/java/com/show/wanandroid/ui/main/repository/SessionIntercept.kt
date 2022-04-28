package com.show.wanandroid.ui.main.repository

import com.show.kcore.extras.log.Logger
import com.show.kcore.http.coroutines.IInterceptHandler
import com.show.kcore.http.coroutines.InterceptState
import com.show.kcore.rden.Stores
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull

class SessionIntercept : IInterceptHandler {


    override suspend fun onIntercept(maxTimeOut:Long,repeatTime:Int,retryCount: Int): InterceptState {
        if(retryCount == repeatTime){
            return InterceptState.ContinueState
        }
        val logInState = withTimeoutOrNull(10000) {
            while (Stores.getString("sessionId", null) == null) {
                Logger.dLog("SessionIntercept","delay ${this@SessionIntercept}")
                delay(100)
            }
            Stores.getString("sessionId", null) != null
        }
        return if (logInState != null && logInState){
            InterceptState.ContinueState
        }else{
            InterceptState.ForceIntercept
        }
    }
}