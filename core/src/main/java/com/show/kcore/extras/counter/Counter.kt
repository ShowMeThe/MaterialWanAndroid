package com.show.kcore.extras.counter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext

/**
 *  showmethe.github.core.util.extras
 *  2020/5/10
 *  11:58
 */

fun LifecycleOwner.waitUtil(outTimeMills:Long,onTime:(result:Boolean)->Unit, doWork: suspend () ->Unit){
    lifecycleScope.launchWhenStarted {
        val result = withTimeoutOrNull(outTimeMills){
            doWork.invoke()
        }
        onTime.invoke(result != null)
    }
}


fun waitUtil(dispatcher: CoroutineContext,outTimeMills:Long,onTime:(result:Boolean)->Unit, doWork: suspend () ->Unit){
    GlobalScope.launch(dispatcher) {
        val result = withTimeoutOrNull(outTimeMills){
            doWork.invoke()
        }
        onTime.invoke(result != null)
    }
}


fun LifecycleOwner.counter(dispatcher: CoroutineContext,start:Int, end:Int, delay:Long, onProgress:((value:Int)->Unit),onFinish: (()->Unit)?= null){
    val out = flow<Int> {
        for (i in start..end) {
            emit(i)
            delay(delay)
        }
    }
    lifecycleScope.launchWhenStarted {
        withContext(dispatcher) {
            out.collect {
                onProgress.invoke(it)
            }
        }
        onFinish?.invoke()
    }
}

fun counter(dispatcher: CoroutineContext,start:Int, end:Int, delay:Long, onProgress:((value:Int)->Unit),onFinish: (()->Unit)?= null){
    val out = flow<Int> {
        for (i in start..end) {
            emit(i)
            delay(delay)
        }
    }
    GlobalScope.launch(dispatcher)  {
        out.collect {
            onProgress.invoke(it)
        }
        onFinish?.invoke()
    }
}

fun LifecycleOwner.timer(dispatcher: CoroutineContext,delay: Long, onFinish: (suspend ()->Unit)?= null){
    lifecycleScope.launchWhenStarted {
        withContext(dispatcher){
            delay(delay)
        }
        onFinish?.invoke()
    }
}


fun timer(dispatcher: CoroutineContext,delay: Long,onFinish: ( suspend ()->Unit)?= null){
    GlobalScope.launch(dispatcher)  {
        delay(delay)
        onFinish?.invoke()
    }
}