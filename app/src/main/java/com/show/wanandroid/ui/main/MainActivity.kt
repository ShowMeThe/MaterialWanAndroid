package com.show.wanandroid.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer

import com.show.wanandroid.R
import com.show.wanandroid.const.HAS_LOGIN
import com.show.wanandroid.const.User_Name
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.ui.main.adapter.MainAdapter
import com.show.wanandroid.ui.main.fragment.HomeFragment
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.extras.post
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.widget.StatusBarUtil.setFullScreen

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {


    private lateinit var adapter: MainAdapter
    private val fragments = ArrayList<Fragment>()
    override fun getViewId(): Int = R.layout.activity_main
    override fun initViewModel(): MainViewModel = createViewModel()
    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.openDrawer.observe(this, Observer {
            it?.apply {
                if(this){
                    drawer.openDrawer(GravityCompat.START)
                }
            }
        })



    }

    override fun init(savedInstanceState: Bundle?) {
        setFullScreen()
        drawer.setScrimColor(Color.TRANSPARENT)



        initAdapter()



    }


    override fun onResume() {
        super.onResume()
        checkLogin()
    }



    override fun initListener() {

        drawer.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {
            }
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                mainLayout.translationX  = slideOffset * drawerView.width
                viewModel.drawerWidth  set  slideOffset
            }
            override fun onDrawerClosed(drawerView: View) {
            }
            override fun onDrawerOpened(drawerView: View) {
            }
        })

        bottomView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.tabHome ->{
                    vp.setCurrentItem(0,true)
                }

                R.id.tabArea ->{

                }

                R.id.tabNav ->{

                }

                R.id.tabPro ->{

                }
            }
            false
        }
    }


    private fun initAdapter(){
        fragments.add(HomeFragment())

        adapter = MainAdapter(fragments,supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vp.adapter = adapter
        vp.offscreenPageLimit = fragments.size
    }



    private fun checkLogin(){
        if(RDEN.get(HAS_LOGIN,false)){
            ivHead.visibility = View.VISIBLE
            tvUserName.visibility = View.VISIBLE
            tvUserName.text = RDEN.get(User_Name,"")
            tvLogin.visibility  = View.GONE
        }else{
            ivHead.visibility = View.GONE
            tvUserName.visibility = View.GONE
            tvLogin.visibility  = View.VISIBLE
        }
    }

}
