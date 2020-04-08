package com.show.wanandroid.ui.main.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.app.BundleCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.show.wanandroid.R
import com.show.wanandroid.const.HAS_LOGIN
import com.show.wanandroid.const.LastFragment
import com.show.wanandroid.const.User_Name
import com.show.wanandroid.databinding.FragmentMainBinding
import com.show.wanandroid.dialog.ThemeDialog
import com.show.wanandroid.themes_name
import com.show.wanandroid.transform.PageTransformer
import com.show.wanandroid.ui.article.fragment.AccountFragment
import com.show.wanandroid.ui.login.LoginActivity
import com.show.wanandroid.ui.main.adapter.MainAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.ui.project.fragment.ProjectFragment
import com.show.wanandroid.ui.tree.fragment.TreeFragment
import com.show.wanandroid.widget.IconSwitch
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.fragment_main.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar


/**
 *  com.show.wanandroid.ui.main.fragment
 *  2020/3/23
 *  23:21
 */
@SuppressLint("SetTextI18n")
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {

    private var currentTag = ""
    private val dialog = ThemeDialog()
    private val interpolator = LinearInterpolator()
    private lateinit var titles : Array<String>
    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_main

    override fun onBundle(bundle: Bundle) {

    }


    override fun observerUI() {

        viewModel.userInfo.observe(this, Observer {
            it?.apply {
                  response?.apply {
                      tvPoint.text = getString(R.string.personal_point) + "$coinCount"
                  }
            }
        })


    }


    override fun init(savedInstanceState: Bundle?) {
        fixToolbar(toolBar)
        binding?.main = this
        titles = arrayOf(getString(R.string.home),getString(R.string.public_),getString(R.string.knowledge),getString(R.string.project))
        drawer.setScrimColor(Color.TRANSPARENT)

        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)

       if(savedInstanceState!=null){
           when(savedInstanceState.getString(LastFragment,HomeFragment::class.java.name)){
               HomeFragment::class.java.name -> bottomView.menu[0].isChecked = true
               AccountFragment::class.java.name ->  bottomView.menu[1].isChecked = true
               TreeFragment::class.java.name ->  bottomView.menu[2].isChecked = true
               ProjectFragment::class.java.name ->  bottomView.menu[3].isChecked = true
           }
       }else{
           replaceFragment(HomeFragment())
       }


        dialog.setThemes()

        initAdapter()

    }

    override fun onVisible() {
        super.onVisible()
        checkLogin()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LastFragment,currentTag)
    }

    override fun initListener() {
        iconSwitch.setOnSwitchClickListener {
            if(it == IconSwitch.STATE_DEFAULT){
                drawer.openDrawer(GravityCompat.START)
            }else{
                drawer.closeDrawer(GravityCompat.START)
            }
        }

        drawer.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {
            }
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                mainLayout.translationX  = slideOffset * drawerView.width
                iconSwitch.transitionPosition = slideOffset
            }
            override fun onDrawerClosed(drawerView: View) {
            }
            override fun onDrawerOpened(drawerView: View) {
            }
        })




        bottomView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.tabHome ->{
                    replaceFragment(HomeFragment())
                    tvTitle.text = titles[0]
                }

                R.id.tabArea ->{
                    replaceFragment(AccountFragment())
                    tvTitle.text = titles[1]
                }

                R.id.tabNav ->{
                    replaceFragment(TreeFragment())
                    tvTitle.text = titles[2]
                }

                R.id.tabPro ->{
                    replaceFragment(ProjectFragment())
                    tvTitle.text = titles[3]
                }
            }
            true
        }


        dialog.setOnThemeClickListener {
            SkinManager.getInstant().switchThemeByName(themes_name[it])
        }


    }


    private fun initAdapter(){


    }


    private fun replaceFragment(replaceFragment : Fragment, id: Int = R.id.frameLayout) {
        val tag = replaceFragment::class.java.name
        currentTag = tag
        var tempFragment = childFragmentManager.findFragmentByTag(tag)
        val transaction = childFragmentManager.beginTransaction()
        if (tempFragment == null) {
            try {
                tempFragment = replaceFragment
                tempFragment.enterTransition = createTransition()
                transaction.addToBackStack(null)
                    .add(id, tempFragment, tag)
                    .setMaxLifecycle(tempFragment, Lifecycle.State.RESUMED)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val fragments = childFragmentManager.fragments

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


    fun search(){
        viewModel.replace set getString(R.string.transition_name_search)
    }


    fun onTheme(){
        dialog.show(childFragmentManager,"theme")
    }

    fun onCollect(){
        viewModel.replace set getString(R.string.transition_name_collect)
    }

    fun onLogin(){
        startActivity<LoginActivity>()
    }


    private fun checkLogin(){
        if(RDEN.get(HAS_LOGIN,false)){
            ivHead.visibility = View.VISIBLE
            tvUserName.visibility = View.VISIBLE
            tvUserName.text = RDEN.get(User_Name,"")
            tvLogin.visibility  = View.GONE
            tvPoint.visibility = View.VISIBLE
            if(viewModel.userInfo.value == null ){
                router.toTarget("getUserInfo")
            }
        }else{
            ivHead.visibility = View.GONE
            tvUserName.visibility = View.GONE
            tvLogin.visibility  = View.VISIBLE
            tvPoint.visibility = View.GONE
        }
    }

}