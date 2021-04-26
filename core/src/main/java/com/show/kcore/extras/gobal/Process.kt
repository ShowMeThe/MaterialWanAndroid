package com.show.kcore.extras.gobal

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.Process
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


class AsyncExecutor : ExecutorImp() {

    companion object {

        private val instant by lazy { AsyncExecutor() }

        private val diskIO =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1)

        private val mainHandler = Handler(Looper.getMainLooper())

        fun executeOnDiskIO(runnable: Runnable): Future<*> {
            return instant.executeOnDiskIO(runnable)
        }

        fun executeOnMainThread(runnable: Runnable) {
            return instant.executeOnMainThread(runnable)
        }

    }

    override fun executeOnDiskIO(runnable: Runnable): Future<*> = diskIO.submit(runnable)

    override fun postToMainThread(runnable: Runnable) {
        mainHandler.post(runnable)
    }

}


abstract class ExecutorImp {

    internal abstract fun executeOnDiskIO(runnable: Runnable): Future<*>

    internal abstract fun postToMainThread(runnable: Runnable)


    internal open fun executeOnMainThread(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
        } else {
            postToMainThread(runnable)
        }
    }

}

fun mainDispatcher(block: () -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        block.invoke()
    }
}

fun LifecycleOwner.mainDispatcher(block: suspend () -> Unit) {
    lifecycleScope.launchWhenCreated {
        withContext(Dispatchers.Main) {
            block.invoke()
        }
    }
}

fun ioDispatcher(block: suspend () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        block.invoke()
    }
}

fun LifecycleOwner.ioDispatcher(block: suspend () -> Unit) {
    lifecycleScope.launchWhenCreated {
        withContext(Dispatchers.IO) {
            block.invoke()
        }
    }
}

fun dispatcher(dispatcher: CoroutineDispatcher, block: suspend () -> Unit) {
    GlobalScope.launch(dispatcher) {
        block.invoke()
    }
}

fun LifecycleOwner.dispatcher(dispatcher: CoroutineDispatcher, block: suspend () -> Unit) {
    lifecycleScope.launchWhenCreated {
        withContext(dispatcher) {
            block.invoke()
        }
    }
}


/**
 * 判断是否主进程
 * @param context 上下文
 * @return true是主进程
 */
fun Context.isMainProcess(main: () -> Unit): Boolean {
    val isMain = isPidOfProcessName(this, getPid(), getMainProcessName(this))
    if (isMain) {
        main.invoke()
    }
    return isMain
}

/**
 * 判断该进程ID是否属于该进程名
 *
 * @param context
 * @param pid 进程ID
 * @param p_name 进程名
 * @return true属于该进程名
 */
private fun isPidOfProcessName(context: Context, pid: Int, p_name: String?): Boolean {
    if (p_name == null) return false
    var isMain = false
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (process in am.runningAppProcesses) {
        if (process.pid == pid) {
            if (process.processName == p_name) {
                isMain = true
            }
            break
        }
    }
    return isMain
}

/**
 * 获取主进程名
 * @param context 上下文
 * @return 主进程名
 */
@Throws(PackageManager.NameNotFoundException::class)
private fun getMainProcessName(context: Context): String? {
    return context.packageManager.getApplicationInfo(context.packageName, 0).processName
}

/**
 * 获取当前进程ID
 * @return 进程ID
 */
private fun getPid(): Int {
    return Process.myPid()
}