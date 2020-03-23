package com.show.wanandroid.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis

import com.show.wanandroid.R
import com.show.wanandroid.const.HAS_LOGIN
import com.show.wanandroid.const.User_Name
import com.show.wanandroid.databinding.ActivityMainBinding
import com.show.wanandroid.transform.PageTransformer
import com.show.wanandroid.ui.article.fragment.AccountFragment
import com.show.wanandroid.ui.main.adapter.MainAdapter
import com.show.wanandroid.ui.main.fragment.HomeFragment
import com.show.wanandroid.ui.main.fragment.MainFragment
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.ui.tree.fragment.TreeFragment
import com.show.wanandroid.widget.IconSwitch
import kotlinx.android.synthetic.main.activity_main.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.extras.post
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbarScreen
import showmethe.github.core.util.widget.StatusBarUtil.setFullScreen

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {

    override fun getViewId(): Int = R.layout.activity_main
    override fun initViewModel(): MainViewModel = createViewModel()
    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

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
        val transition = MaterialSharedAxis.create(context, MaterialSharedAxis.Z, true)
        transition.interpolator = FastOutLinearInInterpolator()
        transition.duration = 450
        transition.addTarget(R.id.treeBody)
        transition.addTarget(R.id.treeArticle)
        return transition
    }


    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

}
