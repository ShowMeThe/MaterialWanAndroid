package com.show.kcore.http

import android.content.Context
import android.util.Log
import com.show.kcore.http.interceptor.ReadCookieInterceptor
import com.show.kcore.http.interceptor.RequestHeaderInterceptor
import com.show.kcore.http.interceptor.RequestLogInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


fun Context.http(baseUrl: String = "", onConfig: (Http.() -> Unit)? = null) {
    val http = Http.getManager()
    http.baseUrl = baseUrl
    http.context = this
    onConfig?.invoke(http)
}

class Http private constructor() {

    companion object {
        private val instant by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Http() }
        fun getManager() = instant

        fun <T> lazyApi(tClass: Class<T>): Lazy<T> {
            return lazy { instant.createApi(tClass) }
        }


        fun <T> createApi(tClass: Class<T>): T {
            return instant.createApi(tClass)
        }
    }

    private var isInit = false
    lateinit var retrofit: Retrofit
    var baseUrl = ""
    lateinit var context: Context

    fun config(onConfig: Config.() -> Unit) {
        if(!isInit){
            val config = Config.get()
            config.baseUrl = baseUrl
            config.context = context.applicationContext
            onConfig(config)
            config.build()
            isInit = true
        }
    }

    fun <T> createApi(tClass: Class<T>): T {
        return retrofit.create(tClass)
    }


    private fun Config.build() {
        val builder = OkHttpClient.Builder()
            .readTimeout(readTimeOut, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(writeTimeOut, TimeUnit.SECONDS)//设置写的超时时间
            .connectTimeout(connectTimeOut.toLong(), TimeUnit.SECONDS)//设置连接超时时间
        interceptors.forEach {
            builder.addInterceptor(it)
        }
        netInterceptors.forEach {
            builder.addNetworkInterceptor(it)
        }
       val client =  builder.cache(Cache(cacheFile,cacheSize)).build()
        val retrofitBuilder = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
        factory.forEach {
            retrofitBuilder.addConverterFactory(it)
        }
        callFactory.forEach {
            retrofitBuilder.addCallAdapterFactory(it)
        }
        retrofit = retrofitBuilder.build()
    }

}

class Config private constructor() {

    companion object {
        private val instant by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Config() }
        fun get() = instant
        private const val CONNECT_TIMEOUT = 30L
        private const val READ_TIMEOUT = 30L
        private const val WRITE_TIMEOUT = 30L
    }

    var baseUrl = ""
    lateinit var context: Context
    val cacheFile by lazy { File(context.externalCacheDir, "NetworkCache") }
    var cacheSize = 50 * 1024 * 1024L
    var connectTimeOut = CONNECT_TIMEOUT
    var readTimeOut = READ_TIMEOUT
    var writeTimeOut = WRITE_TIMEOUT
    var interceptors = mutableListOf<Interceptor>(
        RequestHeaderInterceptor(),
        RequestLogInterceptor(),
        ReadCookieInterceptor()
    )
    var netInterceptors = mutableListOf<Interceptor>()
    var  factory  = mutableListOf<Converter.Factory>(defaultConverterFactory())
    var callFactory = mutableListOf<CallAdapter.Factory>()
    fun defaultConverterFactory() : Converter.Factory{
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
       return MoshiConverterFactory.create(moshi)
           .withNullSerialization().asLenient()
    }

}
