package com.show.kcore.http.interceptor


import androidx.annotation.Keep
import com.show.kcore.extras.log.Logger
import java.io.IOException

import okhttp3.Interceptor


@Keep
class RequestHeaderInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        val originalHttpUrl = request.url
        val htbuilder = originalHttpUrl.newBuilder()


        val url = htbuilder.build()
        val builder = request.newBuilder().url(url)

        builder.addHeader("Content-Type","application/json")
        builder.addHeader("Accept","application/json")

        return chain.proceed(builder.build())
    }
}