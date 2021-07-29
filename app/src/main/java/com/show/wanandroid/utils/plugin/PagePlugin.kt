package com.show.wanandroid.utils.plugin

import androidx.fragment.app.FragmentActivity
import com.show.wanandroid.toast
import com.show.wanandroid.ui.main.WebActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class PagePlugin : FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {

    private var channel: MethodChannel? = null
    private var binding: ActivityPluginBinding? = null

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {

        channel = MethodChannel(binding.binaryMessenger, "nativePage")
        channel?.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {

        channel?.setMethodCallHandler(null)
        channel = null
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "nativePage") {
            val title = call.argument<String>("title")
            val url = call.argument<String>("url")
            if (title != null && url != null){
                this.binding?.apply {
                    WebActivity.start(activity,title,url)
                }

            }
        }
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        this.binding = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        this.binding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
    }
}