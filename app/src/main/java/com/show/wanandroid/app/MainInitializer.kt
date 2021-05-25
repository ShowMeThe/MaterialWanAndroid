package com.show.wanandroid.app

import android.app.Application
import android.content.Context
import androidx.annotation.Keep
import com.show.kcore.http.http
import com.show.kcore.http.interceptor.ReadCookieInterceptor
import com.show.kcore.http.interceptor.RequestLogInterceptor
import com.show.launch.Initializer
import com.show.slideback.SlideRegister
import com.show.wanandroid.DynamicConfigImplDemo
import com.show.wanandroid.TestPluginListener
import com.show.wanandroid.utils.ReadWriteCacheInterceptor
import com.show.wanandroid.utils.RequestNewHeaderInterceptor
import com.tencent.matrix.Matrix
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig
import kotlinx.coroutines.CancellableContinuation


@Keep
class MainInitializer : Initializer<Boolean> {

    override fun onCreate(
        context: Context,
        isMainProcess: Boolean,
        continuation: CancellableContinuation<Boolean>?
    ) {
        SlideRegister.config {
            shadowWidth = 100
        }

        context.http {
            config {
                baseUrl = "https://www.wanandroid.com/"
                interceptors = arrayListOf(
                    RequestNewHeaderInterceptor(), ReadWriteCacheInterceptor(),
                    RequestLogInterceptor(), ReadCookieInterceptor()
                )
            }
        }
    }
}