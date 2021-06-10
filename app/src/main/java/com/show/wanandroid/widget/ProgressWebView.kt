package com.show.wanandroid.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.show.wanandroid.R

class ProgressWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    WebView(context, attrs) {


    init {

        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (url.startsWith("http://") || url.startsWith("https://")) {
                    false

                } else {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                        true
                    } catch (e: Exception) {
                        true
                    }

                }
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    onProgressComplete.invoke()
                }
                super.onProgressChanged(view, newProgress)
            }
        }
    }

    private var onProgressComplete = {
    }

    fun setOnProgressCompleteListener(onProgressComplete: () -> Unit) {
        this.onProgressComplete = onProgressComplete
    }

    /**
     * 通用设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun defaultSetting() {
        val settings = settings
        //设置WebView属性，能够执行Javascript脚本
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.defaultTextEncodingName = "utf-8"
        settings.allowFileAccess = false
        settings.setSupportMultipleWindows(true)
        settings.minimumFontSize = settings.minimumFontSize + 8
        settings.textZoom = 23
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        // 首先使用缓存
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true//启用js
        settings.blockNetworkImage = false//解决图片不显示
        webViewClient = WebViewClient()
    }
}