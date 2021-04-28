package com.show.wanandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.transition.addListener
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.transition.MaterialSharedAxis
import com.show.kcore.base.BaseActivity
import com.show.kcore.base.Transition
import com.show.kcore.base.TransitionMode
import com.show.kcore.extras.binding.DialogFragmentRef
import com.show.kcore.extras.status.statusBar
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.dialog.ThemeDialog
import com.show.wanandroid.getShareViewModel
import com.show.wanandroid.replaceFragment
import com.show.wanandroid.themes_name
import com.show.wanandroid.ui.main.fragment.AccountFragment
import com.show.wanandroid.ui.main.fragment.HomeFragment
import com.show.wanandroid.ui.main.fragment.ProjectFragment
import com.show.wanandroid.ui.main.fragment.TreeFragment
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.widget.IconSwitch
import com.showmethe.skinlib.SkinManager

@Transition(mode = TransitionMode.RevealCenter)
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    private val fragments by lazy {
        arrayListOf(
            HomeFragment(),
            AccountFragment(),
            TreeFragment(),
            ProjectFragment()
        )
    }
    private val dialog by DialogFragmentRef(ThemeDialog::class.java)

    override fun getViewId(): Int = R.layout.activity_main

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {
        statusBar {
            uiFullScreen()
        }

        binding {
            SkinManager.getManager().bindings(this)

            main = this@MainActivity
            executePendingBindings()

        }

        replaceFragment(fragments[0])

    }

    override fun initListener() {

        binding {

            iconSwitch.setOnSwitchClickListener {
                if (it == IconSwitch.STATE_DEFAULT) {
                    drawer.openDrawer(GravityCompat.START)
                } else {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }

            drawer.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    mainLayout.translationX = slideOffset * drawerView.width
                    iconSwitch.transitionPosition = slideOffset
                }

                override fun onDrawerClosed(drawerView: View) {
                }

                override fun onDrawerOpened(drawerView: View) {
                }
            })


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
                        replaceFragment(fragments[3])
                        tvTitle.text = getString(R.string.project)
                    }
                }

                true
            }
        }
    }


    override fun onBackPressed() {
        if (binding.bottomView.selectedItemId == R.id.tabNav
            && getShareViewModel().popBack.value == 2
        ) {
            getShareViewModel().popBack.value = null
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
        }
    }


    fun onTheme(){
        dialog.show(supportFragmentManager,"theme")
        dialog.setOnThemeClickListener {
            SkinManager.getManager().switchThemeByName(themes_name[it])
        }
    }

}