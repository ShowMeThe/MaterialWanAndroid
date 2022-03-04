package com.show.kcore.extras.gobal

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.Process
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


private fun LifecycleOwner.startCoroutineScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    context: CoroutineContext = EmptyCoroutineContext,
    onThrowable: ((context: CoroutineContext, throwable: Throwable?) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val supervisorJob = SupervisorJob(context[Job])
    val scope =
        CoroutineScope(dispatcher + context + supervisorJob + CoroutineExceptionHandler { coroutineContext, throwable ->
            onThrowable?.invoke(coroutineContext, throwable)
        })
    val job = scope.launch {
        block.invoke(this)
    }
    LifecycleController(lifecycle, job)
    return job
}

class LifecycleController(
    private val lifecycle: Lifecycle,
    parentJob: Job
) {

    private val observer = LifecycleEventObserver { source, _ ->
        if (source.lifecycle.currentState <= Lifecycle.State.DESTROYED) {
            finish()
            parentJob.cancel()
        }
    }

    init {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            lifecycle.addObserver(observer)
        } else {
            parentJob.cancel()
        }
    }

    private fun finish() {
        lifecycle.removeObserver(observer)
    }
}


@DelicateCoroutinesApi
fun ViewModel.ioDispatcher(block: suspend () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        block.invoke()
    }
}

@DelicateCoroutinesApi
fun mainDispatcher(block: suspend CoroutineScope. () -> Unit) {
    GlobalScope.launch(Dispatchers.Main.immediate) {
        block.invoke(this)
    }
}

fun LifecycleOwner.mainDispatcher(
    onThrowable: ((context: CoroutineContext, throwable: Throwable?) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
) =
    startCoroutineScope(onThrowable = onThrowable, block = block)

@DelicateCoroutinesApi
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

@DelicateCoroutinesApi
fun ioDispatcher(block: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        block.invoke(this)
    }
}

fun LifecycleOwner.ioDispatcher(
    onThrowable: ((context: CoroutineContext, throwable: Throwable?) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
) =
    startCoroutineScope(Dispatchers.IO, onThrowable = onThrowable, block = block)

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
private fun isPidOfProcessName(
    context: Context,
    pid: Int,
    p_name: String?
): Boolean {
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
    return context.packageManager.getApplicationInfo(
        context.packageName,
        0
    ).processName
}

/**
 * 获取当前进程ID
 * @return 进程ID
 */
private fun getPid(): Int {
    return Process.myPid()
}