package com.show.kcore.extras.gobal

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.annotation.Keep
import com.show.launch.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.system.exitProcess

class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {


    // 系统默认的UncaughtException处理类
    private var mContext: Context? = null

    private var mode = Mode.KeepCrash

    // 用来存储设备信息和异常信息
    private val infos = HashMap<String, String>()

    private val globalpath by lazy { mContext!!.externalCacheDir!!.absolutePath + File.separator + "crash" + File.separator }

    /**
     * 初始化
     *
     * @param context
     */
    private fun init(context: Context) {
        mContext = context

        // 设置该CrashHandler为程序的默认处理器
        Thread.currentThread().uncaughtExceptionHandler = this
        Thread.setDefaultUncaughtExceptionHandler(this)

        if(mode == Mode.KeepAlive){
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                while (true){
                    try {
                        Looper.loop()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
    }




    /**
     * 当UncaughtException发生时会转入该函数来处理
     */

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        handleException(ex)
        if(mode == Mode.KeepCrash){
            Process.killProcess(Process.myPid())
            exitProcess(10)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成
     * @param ex
     * @return true:如果处理了该异常信息; 否则返回false.
     */
    fun handleException(ex: Throwable?): Boolean {
        if (ex == null)
            return false
        try {
            // 收集设备参数信息
            collectDeviceInfo(mContext)
            // 保存日志文件
            saveCrashInfoFile(ex)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */

   private fun collectDeviceInfo(ctx: Context?) {
        try {
            val pm = ctx!!.packageManager
            val pi = pm.getPackageInfo(ctx.packageName,
                PackageManager.GET_ACTIVITIES)
            if (pi != null) {
                val versionName = pi.versionName + ""
                val versionCode = pi.versionCode.toString() + ""
                infos["versionName"] = versionName
                infos["versionCode"] = versionCode
                infos["android version"] = Build.VERSION.RELEASE
                infos["phoneType"] = Build.BRAND + "  " + Build.MODEL
            }
        } catch (e: Exception) {
            Log.e(TAG, "an error occured when collect package info", e)
        }

        val fields = Build::class.java.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                infos[field.name] = field.get(null).toString()
            } catch (e: Exception) {
                Log.e(TAG, "an error occured when collect crash info", e)
            }

        }
    }

    /**
     * 保存错误信息到文件中
     * @param ex
     * @return 返回文件名称,便于将文件传送到服务器
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun saveCrashInfoFile(ex: Throwable): String? {
        val sb = StringBuffer()
        try {
            val sDateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.DEFAULT)
            val date = sDateFormat.format(Date())
            sb.append("\r\n" + date + "\n")
            for ((key, value) in infos) {
                sb.append("$key=$value\n")
            }

            val writer = StringWriter()
            val printWriter = PrintWriter(writer)
            ex.printStackTrace(printWriter)
            var cause: Throwable? = ex.cause
            while (cause != null) {
                cause.printStackTrace(printWriter)
                cause = cause.cause
            }
            printWriter.flush()
            printWriter.close()
            val result = writer.toString()
            sb.append(result)

            return writeFile(sb.toString())
        } catch (e: Exception) {
            writeFile(sb.toString())
        }

        return null
    }

    @Throws(Exception::class)
    private fun writeFile(sb: String): String {
        val time = System.currentTimeMillis().toString()
        val fileName = "crash-$time.xml"
        try {
            val path = globalpath
            val dir = File(path)
            if (!dir.exists())
                dir.mkdirs()
            val fos = FileOutputStream(path + fileName, true)
            fos.write(sb.toByteArray())
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return fileName
    }

    companion object {
        private var TAG = "Crash-handler"

        private var restartAction = "com.crash.restart"
        val instant by lazy { CrashHandler() }


        fun init(ctx: Context,mode: Mode = Mode.KeepCrash): CrashHandler {
            instant.init(ctx)
            instant.mode = mode
            return instant
        }
    }

    @Keep
    enum class Mode{
        KeepAlive,KeepCrash
    }

}