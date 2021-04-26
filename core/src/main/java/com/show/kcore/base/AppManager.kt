package com.show.kcore.base

import android.content.Intent
import java.util.*

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
class AppManager private constructor(){

    companion object {

        internal var stack: Stack<String>? = null

        fun get() : AppManager{
            val instance: AppManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                AppManager() }
            return  instance
        }

        fun finishTarget(cls : Class<*>){
            get().finishTarget(cls)
        }


        fun finishAllWithoutTarget(cls : Class<*>){
            get().finishAllWithoutTarget(cls)
        }
    }



    fun addActivityCls(activityCls: String){
        if(stack == null){
            stack = Stack()
        }
        stack?.apply {
            add(activityCls)
        }
    }

    fun removeActivity(activityCls: String){
        stack?.apply {
            stack?.remove(activityCls)
        }
    }


    fun finishTarget(cls : Class<*>){
        stack?.apply {
            val iterator = iterator()
            while (iterator.hasNext()){
                val name = iterator.next()
                if(name == cls.name){
                    val ctx = AppContext.get().context
                    val intent = Intent(cls.name)
                    ctx.sendBroadcast(intent)
                }
            }
        }
    }

    fun finishAllWithoutTarget(cls : Class<*>){
        stack?.apply {
            val iterator = iterator()
            while (iterator.hasNext()){
                val name = iterator.next()
                if(name != cls.name){
                    val ctx = AppContext.get().context
                    val intent = Intent(cls.name)
                        ctx.sendBroadcast(intent)
                }
            }
        }
    }

}