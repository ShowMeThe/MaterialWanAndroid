package com.show.wanandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.ui.main.fragment.CollectFragment
import com.show.wanandroid.ui.main.fragment.MainFragment

import com.show.wanandroid.ui.main.vm.MainViewModel
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.widget.StatusBarUtil.setFullScreen

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {


    private  val interpolator = LinearInterpolator()
    override fun getViewId(): Int = R.layout.activity_main
    override fun initViewModel(): MainViewModel = createViewModel()
    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

        viewModel.replace.observe(this, Observer {
            it?.apply {
                when(this){
                    getString(R.string.transition_name_search) -> startActivity<SearchActivity>()
                    getString(R.string.transition_name_collect) -> replaceFragment(CollectFragment())
                }
            }
        })

    }


    override fun init(savedInstanceState: Bundle?) {
       setFullScreen()

       replaceFragment(MainFragment())


    }

    override fun initListener() {

    }


    private fun replaceFragment(replaceFragment : Fragment, id: Int = R.id.frameLayout) {
        val tag = replaceFragment::class.java.name
        var tempFragment = supportFragmentManager.findFragmentByTag(tag)
        val transaction = supportFragmentManager.beginTransaction()
        if (tempFragment == null) {
            try {
                tempFragment = replaceFragment
                tempFragment.enterTransition = createTransition()
                tempFragment.exitTransition = createTransition()
                transaction.addToBackStack(null)
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
                    .show(fragment)
            } else {
                transaction
                    .hide(fragment)
            }
        }
        transaction.commitAllowingStateLoss()
    }

    private fun createTransition(): Transition {
        val fade = Fade()
        fade.duration = 250
        fade.interpolator = interpolator
        return fade
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size > 1){
            supportFragmentManager.popBackStack()
        }else{
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
        }
    }

}
