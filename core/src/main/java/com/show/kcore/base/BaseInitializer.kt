package com.show.kcore.base

import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import com.show.kcore.extras.blur.BlurK
import com.show.kcore.extras.gobal.CrashHandler
import com.show.kcore.rden.Stores
import com.show.launch.Initializer
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
        AppContext.get().attach(context)
        BlurK.init(context)
        Stores.initialize(context)
        CrashHandler.init(context,CrashHandler.Mode.KeepAlive)
    }
}