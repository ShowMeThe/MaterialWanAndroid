package com.show.wanandroid.ui.login

import android.os.Bundle
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivityLoginBinding
import com.show.wanandroid.ui.login.fragment.LoginContainerFragment
import com.show.wanandroid.ui.login.fragment.LoginInFragment
import com.show.wanandroid.ui.login.vm.LoginViewModel
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.activity_login.*

import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbarScreen
import showmethe.github.core.util.widget.StatusBarUtil.registerActivity



/**
 * 登录和注册采用Fragment + MaterialTransition完成
 */
class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>() {


    override fun getViewId(): Int = R.layout.activity_login
    override fun initViewModel(): LoginViewModel = createViewModel()
    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

        viewModel.replaceFragment.observe(this, Observer {
            it?.apply {
                when(this){
                    1,2 ->{
                        //登录
                        replaceFragment(LoginInFragment::class.java.name)
                    }
                }
            }
        })

        viewModel.backPress.observe(this, Observer {
            it?.apply {
                if(this){
                    if(supportFragmentManager.fragments.size >= 2){
                        supportFragmentManager.popBackStack()
                    }
                }
            }
        })


    }

    override fun init(savedInstanceState: Bundle?) {
        fixToolbarScreen(toolBar)
        registerActivity(true)
        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)

        replaceFragment(LoginContainerFragment::class.java.name)

    }

    override fun initListener() {




    }


    private fun replaceFragment(tag: String, id: Int = R.id.frameLayout) {
        var tempFragment = supportFragmentManager.findFragmentByTag(tag)
        val transaction = supportFragmentManager.beginTransaction()
        if (tempFragment == null) {
            try {
                tempFragment = Class.forName(tag).newInstance() as Fragment
                tempFragment.enterTransition = createTransition()
                transaction
                    .addToBackStack(null)
                    .add(id, tempFragment, tag)
                    .setMaxLifecycle(tempFragment, Lifecycle.State.RESUMED)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val fragments = supportFragmentManager.fragments
        for (i in fragments.indices) {
            val fragment = fragments[i]
            if (fragment.tag == tag) {
                transaction
                    .addToBackStack(null)
                    .show(fragment)
            } else {
                transaction
                    .addToBackStack(null)
                    .hide(fragment)
            }
        }
        transaction.commitAllowingStateLoss()
    }


    private fun createTransition(): MaterialSharedAxis? {
        val transition = MaterialSharedAxis.create(this, MaterialSharedAxis.Z, true)
        transition.interpolator = AnticipateOvershootInterpolator()
        transition.duration = 400
        transition.addTarget(R.id.login_container)
        transition.addTarget(R.id.login_layout)
        return transition
    }

    private fun createAlphaTransition(): MaterialFade? {
        val transition = MaterialFade.create(this)
        transition.interpolator = LinearInterpolator()
        transition.duration = 400
        transition.addTarget(R.id.login_container)
        transition.addTarget(R.id.login_layout)
        return transition
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size >= 2){
            viewModel.backPress set true
        }else{
             super.onBackPressed()
        }
    }
}