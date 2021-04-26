package com.show.kcore.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import com.show.kcore.rden.Stores


class ReadCookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url.toString()
        val host = request.url.host
        if(requestUrl.contains("user/login") && response.headers("set-cookie").isNotEmpty()){
            val cookies = response.headers("set-cookie")
            encodeCookie(cookies)
        }
        return response
    }
}

fun encodeCookie(cookies:List<String>){
    cookies.forEach Continuing@{
        if(it.contains("JSESSIONID")){
            val cookie = it.substringAfter("JSESSIONID=").substringBefore(";")
            Stores.put("sessionId",cookie)
            return@Continuing
        }
    }
}