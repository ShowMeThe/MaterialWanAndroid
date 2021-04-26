package com.show.wanandroid.ui.main

import android.os.Bundle
import com.show.kcore.base.BaseActivity
import com.show.kcore.base.Transition
import com.show.kcore.base.TransitionMode
import com.show.kcore.extras.status.statusBar
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.replaceFragment
import com.show.wanandroid.ui.main.fragment.HomeFragment
import com.show.wanandroid.ui.main.vm.MainViewModel

@Transition(mode = TransitionMode.RevealCenter)
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    private val fragments by lazy { arrayListOf(HomeFragment()) }

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
                    }
                    R.id.tabArea -> {

                    }
                    R.id.tabNav -> {

                    }
                    R.id.tabPro -> {

                    }
                }

                true
            }
        }
    }
}