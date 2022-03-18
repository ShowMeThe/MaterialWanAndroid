package com.show.wanandroid.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.annotation.FloatRange
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.show.kcore.base.BaseActivity
import com.show.kcore.base.startActivity
import com.show.kcore.extras.binding.DialogFragmentRef
import com.show.kcore.extras.display.dp
import com.show.kcore.extras.display.screenH
import com.show.kcore.extras.display.screenW
import com.show.kcore.extras.status.statusBar
import com.show.kcore.rden.Stores
import com.show.slideback.annotation.SlideBackPreview
import com.show.wanandroid.*
import com.show.wanandroid.bean.UserBean
import com.show.wanandroid.const.StoreConst
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.dialog.ExitDialog
import com.show.wanandroid.dialog.ThemeDialog
import com.show.wanandroid.flutter.FlutterRouter
import com.show.wanandroid.ui.main.fragment.AccountFragment
import com.show.wanandroid.ui.main.fragment.HomeFragment
import com.show.wanandroid.ui.main.fragment.ProjectFragment
import com.show.wanandroid.ui.main.fragment.TreeFragment
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.widget.IconSwitch
import com.show.wanandroid.widget.overlap.OverLap
import com.show.wanandroid.widget.overlap.level
import com.show.wanandroid.widget.overlap.widget.BubbleDirection
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

        if(Stores.getBoolean(StoreConst.ShowMask,false).not()){
            Stores.put(StoreConst.ShowMask,true)
            showGuideMask()
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

    private fun showGuideMask(){
        OverLap.builder(this.window)
            .levels(level {
                child {
                    center(25f.dp,55f.dp)
                    radius(35f.dp)
                    labelText("左滑菜单", BubbleDirection.LEFT)
                    labelColor(Color.WHITE)
                    labelWidthHeight(100f.dp,65f.dp)
                }
                child {
                    center(screenW.toFloat() - 25f.dp,55f.dp)
                    radius(35f.dp)
                    labelText("搜索", BubbleDirection.RIGHT)
                    labelColor(Color.WHITE)
                    labelWidthHeight(100f.dp,65f.dp)
                }

            }, level {
                child {
                    center(screenW.toFloat() - 50f.dp, screenH.toFloat() - 95f.dp)
                    radius(45f.dp)
                    labelText("悬浮按钮", BubbleDirection.RIGHT)
                    labelColor(Color.WHITE)
                    labelWidthHeight(100f.dp,65f.dp)
                }
            })
            .build()
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

    fun onFlutter() {
//        startActivity<FlutterMainActivity>(transition = false)
        FlutterRouter.goto(this)
    }

}