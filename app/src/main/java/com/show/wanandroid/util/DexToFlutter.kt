package com.show.wanandroid.util

import android.content.Context
import android.content.Intent
import android.util.Log
import dalvik.system.DexClassLoader
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object DexToFlutter {

    fun goToFlutter(context: Context){
        val cacheDir = context.externalCacheDir!!.path
        val flutterApk = File("$cacheDir/flutter.apk")
        if(flutterApk.exists()){
            dexApk(context,flutterApk)
        }else{
            val ins = context.assets.open("flutter.apk")
            val out = BufferedOutputStream(FileOutputStream(flutterApk.path))
            val buf = ByteArray(4096)
            var len = 0
            while((ins.read(buf).also { len =  it})!=-1){
                out.write(buf,0,len)
            }
            out.flush()
            out.close()
            ins.close()
            dexApk(context,flutterApk)
        }

    }

    private fun dexApk(context: Context,file: File) {
        val dexClassLoader = DexClassLoader(file.path,context.externalCacheDir!!.path,null,
            ClassLoader.getSystemClassLoader())
        val clazz = dexClassLoader.loadClass("com.ken.androidflutter.MainActivity")

    }

}