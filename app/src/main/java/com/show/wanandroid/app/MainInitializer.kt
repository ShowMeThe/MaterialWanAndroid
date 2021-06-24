package com.show.wanandroid.app

import android.content.Context
import androidx.annotation.Keep
import com.show.kcore.extras.display.dp
import com.show.kcore.http.http
import com.show.kcore.http.interceptor.ReadCookieInterceptor
import com.show.kcore.http.interceptor.RequestLogInterceptor
import com.show.launch.Initializer
import com.show.slideback.SlideRegister
import com.show.wanandroid.utils.ReadWriteCacheInterceptor
import com.show.wanandroid.utils.RequestNewHeaderInterceptor
import kotlinx.coroutines.CancellableContinuation


@Keep
class MainInitializer : Initializer<Boolean> {

    override fun onCreate(
        context: Context,
        isMainProcess: Boolean,
        continuation: CancellableContinuation<Boolean>?
    ) {
        SlideRegister.config {
            shadowWidth = 30f.dp.toInt()
            maxSideLength = 25f.dp
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