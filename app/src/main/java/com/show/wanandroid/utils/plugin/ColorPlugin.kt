package com.show.wanandroid.utils.plugin

import com.show.wanandroid.toast
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec




class ColorPlugin : FlutterPlugin, MethodChannel.MethodCallHandler {

    private var channel: MethodChannel? = null
    private var messageChannel : BasicMessageChannel<Any>? = null

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, "color")
        channel?.setMethodCallHandler(this)

        messageChannel = BasicMessageChannel(binding.binaryMessenger,"colorChange",
            StandardMessageCodec())
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
        channel = null
        messageChannel = null
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {

    }

    fun updateColor(index:Int){
        messageChannel?.send(index)
    }
}