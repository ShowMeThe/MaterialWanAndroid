package showmethe.github.core.util.system

import android.os.Build
import android.os.Environment
import android.util.Log

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:56
 * Package Name:showmethe.github.core.util.system
 */
/**
 * 适配国内的手机
 */
class GetSystemUtils {

    companion object{
        private var system = ""

        val xiaomi = "xiaomi"
        val oppo = "oppo"
        val meizu = "meizu"


        fun getSystem() : String{
            if(system.isEmpty()){
                system = android.os.Build.MANUFACTURER.toLowerCase(Locale.getDefault())
            }
            return system
        }
    }

}
