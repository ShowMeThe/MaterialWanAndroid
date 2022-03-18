package com.show.wanandroid.ui.main

import android.os.Bundle
import com.show.wanandroid.utils.plugin.PagePlugin
import com.show.wanandroid.utils.plugin.ToastPlugin
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine


class FlutterMainActivity : FlutterActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flutterEngine?.apply {
            plugins.add(ToastPlugin())
            plugins.add(PagePlugin())

        }



    }

}


