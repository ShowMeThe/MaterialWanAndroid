package showmethe.github.core.util.system

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import showmethe.github.core.http.RetroHttp
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.system
 */

/**
 * 网络连接状态
 * @param context
 * @return
 */
fun checkConnection(context: Context): Boolean {
   return try {
      val cm = context
         .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val networkInfo = cm.activeNetworkInfo
      !(networkInfo == null || !networkInfo.isConnected)
   }catch (e:Exception){
       false
   }
}

fun startLocalForIp(){
    Network.get()
}


class Network{
    var networkState = true
    @Volatile
    private var requestTime = 1
    companion object{
        private val instant : Network by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Network() }
        fun get() = instant
    }

    @Synchronized
    fun addTime(){
        startCheck()
        requestTime ++
    }

    private fun startCheck(){
       if(requestTime%150 == 0){
           GlobalScope.launch(Dispatchers.IO){
               requestTime = 0
               pingIP(this.coroutineContext,RetroHttp.baseUrl
                   .substringAfter("http://")
                   .substringAfter("https://")
                   .substringBefore("/"))
           }
       }
    }
}

 suspend fun pingIP(context: CoroutineContext,address:String) : Boolean{
    return withContext(context){
         try {
             val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 1 $address")
             val status = process.waitFor()
             Log.e("NetworkWork","ping for network for status =  ${status == 0}")
             Network.get().networkState = status == 0
             status == 0
         }catch (e : Exception){
             Network.get().networkState = false
             false
         }
     }
}


