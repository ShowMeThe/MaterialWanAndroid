package com.show.wanandroid.app

import android.content.Context
import androidx.annotation.Keep
import com.show.kcore.http.http
import com.show.launch.Initializer
import com.show.slideback.SlideRegister
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

        context.http{
            config {
                baseUrl = "https://www.wanandroid.com/"
            }
        }
    }
}