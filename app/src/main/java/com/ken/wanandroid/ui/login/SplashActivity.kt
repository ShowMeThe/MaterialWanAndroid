package com.ken.wanandroid.ui.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.util.Pair
import com.ken.wanandroid.R
import com.ken.wanandroid.databinding.ActivitySplashBinding
import com.ken.wanandroid.ui.login.vm.LoginViewModel

import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.kinit.Module
import showmethe.github.core.kinit.startInit
import showmethe.github.core.util.extras.float2Decimal
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.widget.StatusBarUtil.setFullScreen
import kotlin.random.Random

class SplashActivity : BaseActivity<ActivitySplashBinding,LoginViewModel>() {


    override fun setTheme() {
        setFullScreen()
    }
    override fun getViewId(): Int = R.layout.activity_splash

    override fun initViewModel(): LoginViewModel = createViewModel()

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

    }

    override fun init(savedInstanceState: Bundle?) {

        motion.transitionToEnd()
        GlobalScope.launch(Dispatchers.Main) {
            delay(2500)
            startActivityWithPair<LoginActivity>()
            finishAfterTransition()
        }
    }

    override fun initListener() {
    }



}
