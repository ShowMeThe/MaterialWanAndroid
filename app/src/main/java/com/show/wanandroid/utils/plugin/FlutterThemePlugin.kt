package com.show.wanandroid.utils.plugin

import com.show.kcore.rden.Stores
import com.show.wanandroid.themes_name
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

class FlutterThemePlugin : FlutterPlugin,MethodChannel.MethodCallHandler {

    private var channel: MethodChannel? = null

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        call.method?.apply {
            if(this == "getTheme"){
                result.success(Stores.getString("theme", themes_name[0]))
            }
        }
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger,"theme")
        channel?.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
        channel = null
    }
}