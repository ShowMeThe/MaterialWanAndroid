package showmethe.github.core.util.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import showmethe.github.core.base.ContextProvider

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.widget
 */

class ScreenSizeUtil{

   companion object {
       var needRefresh = true
       var width = 0
       var height  = 0
       private val instant by lazy { ScreenSizeUtil() }
       fun get() = instant
   }

    fun getWH(context: Context){
        if(needRefresh){
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            manager.defaultDisplay.getMetrics(dm)
            width = dm.widthPixels
            height = dm.heightPixels
            needRefresh = false
        }

    }


    private var rotation = 0
    fun init(context: Context):ScreenSizeUtil{
        val broadcastReceive = ScreenReceiver()
        val filter =  IntentFilter()
        filter.addAction("android.intent.action.CONFIGURATION_CHANGED")
        context.applicationContext.registerReceiver(broadcastReceive,filter)
        return this
    }


    inner class ScreenReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            ContextProvider.get().getActivity()?.apply {
                val newRotation = windowManager.defaultDisplay.rotation * 90
                if(rotation != newRotation){
                    rotation = newRotation
                    needRefresh = true
                    getWH(this)
                }
            }
        }
    }


}