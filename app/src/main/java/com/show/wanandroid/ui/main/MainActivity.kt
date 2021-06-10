package com.show.wanandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
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
import com.show.kcore.rden.Stores
import com.show.slideback.annotation.SlideBackPreview
import com.show.wanandroid.*
import com.show.wanandroid.bean.UserBean
import com.show.wanandroid.const.StoreConst
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.dialog.ExitDialog
import com.show.wanandroid.dialog.LoadingDialog
import com.show.wanandroid.dialog.ThemeDialog
import com.show.wanandroid.ui.main.fragment.AccountFragment
import com.show.wanandroid.ui.main.fragment.HomeFragment
import com.show.wanandroid.ui.main.fragment.ProjectFragment
import com.show.wanandroid.ui.main.fragment.TreeFragment
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.widget.IconSwitch
import com.showmethe.skinlib.SkinManager

@SlideBackPreview
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    private val fragments by lazy {
        arrayListOf(
            HomeFragment(),
            AccountFragment(),
            TreeFragment(),
            ProjectFragment()
        )
    }

    private val exitDialog by DialogFragmentRef(ExitDialog::class.java)
    private val dialog by DialogFragmentRef(ThemeDialog::class.java)

    override fun getViewId(): Int = R.layout.activity_main

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        Stores.getLive<UserBean>(this, StoreConst.UserInfo) {
            binding{
                tvUser.text = it?.account ?: getString(R.string.login)

                ivOut.visibility = if(it != null){
                    View.VISIBLE
                }else{
                    View.GONE
                }
            }
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        statusBar {
            uiFullScreen()
        }

        binding {
            SkinManager.getManager().autoTheme(SkinManager.currentStyle, binding)

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




            bottomView.setOnNavigationSingleItemSelectedListener {
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

    fun onSearch(){
        startActivityWithPair<SearchActivity>(null,Pair(binding.ivSearch,getString(R.string.transition_name_search)))
    }


    fun onLogin() {
        if(Stores.getBoolean(StoreConst.IsLogin,false).not()){
            startActivity<LoginActivity>(transition = true)
        }
    }

    fun exit(){
        exitDialog.show(supportFragmentManager,"exitDialog")
        exitDialog.setOnConfirmClickListener {
            logOut()
        }

    }

    fun onTheme() {
        dialog.show(supportFragmentManager, "theme")
        dialog.setOnThemeClickListener {
            SkinManager.getManager().switchThemeByName(themes_name[it])
        }
    }

    fun onCollect() {
        if(Stores.getBoolean(StoreConst.IsLogin,false).not()){
            startActivity<LoginActivity>(transition = true)
        }else{
            startActivity<CollectActivity>(transition = true)
        }
    }

}