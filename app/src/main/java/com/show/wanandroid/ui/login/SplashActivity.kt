package com.show.wanandroid.ui.login

import android.os.Bundle
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivitySplashBinding
import com.show.wanandroid.ui.login.vm.LoginViewModel
import com.show.wanandroid.ui.main.MainActivity
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.widget.StatusBarUtil.setFullScreen

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
        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)

        motion.transitionToEnd()
        GlobalScope.launch(Dispatchers.Main) {
            delay(2500)
            startActivityWithPair<MainActivity>()
            finishAfterTransition()
        }
    }

    override fun initListener() {
    }



}
