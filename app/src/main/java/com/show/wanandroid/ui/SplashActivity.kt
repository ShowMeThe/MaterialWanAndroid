package com.show.wanandroid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.show.kcore.extras.gobal.mainDispatcher
import com.show.kcore.extras.status.statusBar
import com.show.wanandroid.databinding.ActivitySplashBinding
import com.show.wanandroid.ui.main.MainActivity
import kotlinx.coroutines.delay


class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBar {
            uiFullScreen(false)
        }

        binding.apply {

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