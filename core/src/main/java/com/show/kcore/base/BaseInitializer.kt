package com.show.kcore.base

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import com.show.kInject.core.initScope
import com.show.kcore.extras.blur.BlurK
import com.show.kcore.extras.gobal.CrashHandler
import com.show.kcore.rden.Stores
import com.show.launch.Initializer
import com.show.launch.InitializerType
import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume

/**
 *  com.show.kcore.base
 *  2020/8/2
 *  0:20
 *  ShowMeThe
 */
@Keep
class BaseInitializer  : Initializer<Boolean> {

    override fun onCreate(
        context: Context,
        isMainProcess: Boolean,
        continuation: CancellableContinuation<Boolean>?
    ) {
        initScope { androidContext(context.applicationContext as Application) }
        AppContext.get().attach(context)
        Stores.initialize(context)
    }
}

@Keep
class AsyncInitializer  : Initializer<Boolean> {

    override fun onCreate(
        context: Context,
        isMainProcess: Boolean,
        continuation: CancellableContinuation<Boolean>?
    ) {
        BlurK.init(context)
        CrashHandler.init(context,CrashHandler.Mode.KeepAlive)
        continuation?.resume(true)
    }

    override fun initializerType(): InitializerType = InitializerType.Async
}