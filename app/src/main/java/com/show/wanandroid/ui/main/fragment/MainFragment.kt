package com.show.wanandroid.ui.main.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Pair
import android.view.View
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.transition.MaterialContainerTransform
import com.show.wanandroid.R
import com.show.wanandroid.const.HAS_LOGIN
import com.show.wanandroid.const.User_Name
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.databinding.FragmentMainBinding
import com.show.wanandroid.transform.PageTransformer
import com.show.wanandroid.ui.article.fragment.AccountFragment
import com.show.wanandroid.ui.main.adapter.MainAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.ui.project.fragment.ProjectFragment
import com.show.wanandroid.ui.tree.fragment.TreeFragment
import com.show.wanandroid.widget.IconSwitch
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
class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>() {


    private lateinit var titles : Array<String>
    private lateinit var adapter: MainAdapter
    private val fragments = ArrayList<Fragment>()
    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_main

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

    }


    override fun init(savedInstanceState: Bundle?) {
        fixToolbar(toolBar)
        binding?.main = this
        titles = arrayOf(getString(R.string.home),getString(R.string.public_),getString(R.string.knowledge),getString(R.string.project))
        drawer.setScrimColor(Color.TRANSPARENT)

        initAdapter()

    }

    override fun onVisible() {
        super.onVisible()
        checkLogin()
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


        vp.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                bottomView.menu.getItem(position).isChecked = true
                tvTitle.text = titles[position]
            }
        })

        bottomView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.tabHome ->{
                    vp.setCurrentItem(0,false)
                }

                R.id.tabArea ->{
                    vp.setCurrentItem(1,false)
                }

                R.id.tabNav ->{
                    vp.setCurrentItem(2,false)
                }

                R.id.tabPro ->{
                    vp.setCurrentItem(3,false)
                }
            }
            tvTitle.text = titles[vp.currentItem]
            false
        }
    }


    private fun initAdapter(){
        fragments.add(HomeFragment())
        fragments.add(AccountFragment())
        fragments.add(TreeFragment())
        fragments.add(ProjectFragment())
        adapter = MainAdapter(fragments,childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vp.adapter = adapter
        vp.offscreenPageLimit = fragments.size
        vp.setPageTransformer(false, PageTransformer())
    }




    fun search(){
        viewModel.replace set "Search"
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