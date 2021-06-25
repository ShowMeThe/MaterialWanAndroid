package com.show.wanandroid.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import com.show.wanandroid.utils.plugin.FlutterThemePlugin
import com.show.wanandroid.utils.plugin.ToastPlugin
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.plugins.util.GeneratedPluginRegister
import io.flutter.plugins.GeneratedPluginRegistrant


class FlutterMainActivity : FlutterActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flutterEngine?.apply {
            plugins.add(FlutterThemePlugin())
            plugins.add(ToastPlugin())
        }

    }

}