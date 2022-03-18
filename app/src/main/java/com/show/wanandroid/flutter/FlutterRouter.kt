package com.show.wanandroid.flutter

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.show.wanandroid.utils.plugin.ColorPlugin
import com.show.wanandroid.utils.plugin.PagePlugin
import com.show.wanandroid.utils.plugin.ToastPlugin
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

object FlutterRouter {

    const val FLUTTER_ENGINE = "FLUTTER_ENGINE"

    private val colorPlugin = ColorPlugin()

    fun preload(context: Context):Boolean{
        return kotlin.runCatching {
            val flutterEngine = FlutterEngine(context)
            flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())
            FlutterEngineCache.getInstance().put(FLUTTER_ENGINE,flutterEngine)
            flutterEngine.apply {
                plugins.add(ToastPlugin())
                plugins.add(PagePlugin())
                plugins.add(colorPlugin)
            }
            true
        }.onFailure {
            it.printStackTrace()
        }.getOrDefault(false)
    }

    fun updateColor(index:Int){
        colorPlugin.updateColor(index)
    }

    fun goto(context: AppCompatActivity){
        context.startActivity(FlutterActivity.withCachedEngine(FLUTTER_ENGINE).build(context))
    }

}