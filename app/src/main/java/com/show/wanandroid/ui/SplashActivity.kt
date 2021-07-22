package com.show.wanandroid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.shape.MaterialShapeDrawable
import com.show.kcore.extras.gobal.mainDispatcher
import com.show.kcore.extras.status.statusBar
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivitySplashBinding
import com.show.wanandroid.ui.main.MainActivity
import com.showmethe.skinlib.SkinManager
import kotlinx.coroutines.delay


class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this,R.layout.activity_splash)

        statusBar {
            uiFullScreen(false)
        }

        binding.apply {
            SkinManager.getManager().autoTheme(SkinManager.currentStyle,binding)
            jump.startDrawing()

            mainDispatcher {
                delay(1000)
                startActivity(
                    Intent(this@SplashActivity, MainActivity::class.java)
                )
                finishAfterTransition()
            }


        }

    }
}