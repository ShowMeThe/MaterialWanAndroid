package com.show.wanandroid.utils

import androidx.annotation.Keep
import com.show.kcore.extras.log.Logger
import com.show.kcore.rden.Stores
import okhttp3.Interceptor
import java.io.IOException

@Keep
class RequestNewHeaderInterceptor : Interceptor {

    var sessionId = ""

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val originalHttpUrl = request.url
        val htbuilder = originalHttpUrl.newBuilder()


        val url = htbuilder.build()
        val builder = request.newBuilder().url(url)

        if (this.sessionId.isEmpty()) {
            this.sessionId = Stores.getString("sessionId", "") ?: ""
            Logger.dLog("RequestNewHeaderInterceptor","JSESSIONID=$sessionId")
        }
        builder.addHeader("Cookie", "JSESSIONID=$sessionId")
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept", "application/json")
        return chain.proceed(builder.build())
    }
}