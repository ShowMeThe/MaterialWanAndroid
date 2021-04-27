package com.show.wanandroid.ui.main

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.transition.addListener
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.transition.MaterialSharedAxis
import com.show.kcore.base.BaseActivity
import com.show.kcore.base.Transition
import com.show.kcore.base.TransitionMode
import com.show.kcore.extras.status.statusBar
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.getShareViewModel
import com.show.wanandroid.replaceFragment
import com.show.wanandroid.ui.main.fragment.AccountFragment
import com.show.wanandroid.ui.main.fragment.HomeFragment
import com.show.wanandroid.ui.main.fragment.TreeFragment
import com.show.wanandroid.ui.main.vm.MainViewModel

@Transition(mode = TransitionMode.RevealCenter)
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    private val fragments by lazy { arrayListOf(HomeFragment(), AccountFragment(), TreeFragment()) }

    override fun getViewId(): Int = R.layout.activity_main

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {
        statusBar {
            uiFullScreen()
        }


        replaceFragment(fragments[0])

    }

    override fun initListener() {

        binding {


            bottomView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.tabHome -> {
                        replaceFragment(fragments[0])
                        tvTitle.text = getString(R.string.home)
                    }
                    R.id.tabArea -> {
                        replaceFragment(fragments[1])
                        tvTitle.text = getString(R.string.public_)
                    }
                    R.id.tabNav -> {
                        replaceFragment(fragments[2])
                        tvTitle.text = getString(R.string.knowledge)
                    }
                    R.id.tabPro -> {

                        tvTitle.text = getString(R.string.project)
                    }
                }

                true
            }
        }
    }


    override fun onBackPressed() {
        if(binding.bottomView.selectedItemId == R.id.tabNav
            && getShareViewModel().popBack.value == 2){
            getShareViewModel().popBack.value = null
        }else{
            super.onBackPressed()
        }
    }

}