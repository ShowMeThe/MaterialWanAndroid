package com.show.kcore.extras.log

import android.util.Log
import com.show.kcore.BuildConfig



object Logger {

    enum class LogType {
        e,d,w,i,v
    }

    fun eLog(tag:String,any: Any?){
        if(BuildConfig.LOG_ENABLE){
            spiltPrint(LogType.e,tag,any)
        }
    }

    fun dLog(tag:String,any: Any?){
        if(BuildConfig.LOG_ENABLE){
            spiltPrint(LogType.d,tag,any)
        }
    }

    fun wLog(tag:String,any: Any?){
        if(BuildConfig.LOG_ENABLE){
            spiltPrint(LogType.w,tag,any)
        }
    }

    fun iLog(tag:String,any: Any?){
        if(BuildConfig.LOG_ENABLE){
            spiltPrint(LogType.i,tag,any)
        }
    }

    fun vLog(tag:String,any: Any?){
        if(BuildConfig.LOG_ENABLE){
            spiltPrint(LogType.v,tag,any)
        }
    }


    private fun spiltPrint(type: LogType,tag: String,any: Any?){
        if(any!=null){
            val maxLogSize = 1000
            val longString = any.toString()
            for (i in 0 .. longString.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > longString.length) longString.length else end
                print(type,tag,longString.substring(start, end))
            }
        }else{
            print(type,tag,"null")
        }
    }

    private fun print(type: LogType,tag: String,result:String){
        when(type){
            LogType.e -> Log.e(tag,result)
            LogType.d -> Log.d(tag,result)
            LogType.w -> Log.w(tag,result)
            LogType.i -> Log.i(tag,result)
            LogType.v -> Log.v(tag,result)
        }
    }
}