package com.show.wanandroid.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.show.kcore.base.*
import com.show.kcore.extras.status.statusBar
import com.show.slideback.annotation.SlideBackBinder
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivityWebBinding
import com.showmethe.skinlib.SkinManager
import kotlin.math.acos

@SlideBackBinder
@Transition(mode = TransitionMode.SlideStart)
class WebActivity : AppCompatActivity() {

    companion object {
        fun start(context: FragmentActivity, title: String, url: String) {
            val bundle = Bundle().apply {
                putString("title",title)
                putString("url",url)
            }
            context.startActivity<WebActivity>(bundle,true)
        }

        fun start(context: Activity, title: String, url: String) {
            val bundle = Bundle().apply {
                putString("title",title)
                putString("url",url)
            }
            val intent = Intent(context,WebActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    @BundleInject("title")
    private var title = ""

    @BundleInject("url")
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityWebBinding>(this,R.layout.activity_web)
        setUpTransition()

        intent.apply {
            extras?.apply {
                BundleAutoInject.inject(this@WebActivity)
            }
        }

        statusBar {
            uiFullScreen(true)
        }


        binding.apply {
            SkinManager.getManager().autoTheme(SkinManager.currentStyle,this)

            main = this@WebActivity
            executePendingBindings()


            tvTitle.text = title

            smart.showLoading()
            webView.loadUrl(url)
            webView.defaultSetting()
            webView.setOnProgressCompleteListener {
                smart.showContent()
            }


        }


    }
}