package com.show.wanandroid.ui.tree.fragment

import android.os.Bundle
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentTreeBinding
import com.show.wanandroid.ui.main.vm.MainViewModel
import showmethe.github.core.base.BaseFragment

/**
 *  com.show.wanandroid.ui.tree.fragment
 *  2020/3/21
 *  10:34
 */
class TreeFragment : BaseFragment<FragmentTreeBinding, MainViewModel>() {


    override fun initViewModel(): MainViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_tree

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

        viewModel.treeNavigator.observe(this, Observer {
            it?.apply {
                val fragment = TreeArticleFragment.get(it.first,it.second)
                replaceFragment(fragment)
            }
        })

        viewModel.treeNavBack.observe(this, Observer {
            it?.apply {
                if(this){
                    if(childFragmentManager.fragments.size == 2){
                        childFragmentManager.popBackStack()
                    }
                }
            }
        })

    }

    override fun init(savedInstanceState: Bundle?) {

        replaceFragment(TreeBodyFragment())

    }

    override fun initListener() {





    }

    private fun replaceFragment(replaceFragment : Fragment, id: Int = R.id.frameLayout) {
        val tag = replaceFragment::class.java.name
        var tempFragment = childFragmentManager.findFragmentByTag(tag)
        val transaction = childFragmentManager.beginTransaction()
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
        val fragments = childFragmentManager.fragments
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
        val transition = MaterialSharedAxis.create(context, MaterialSharedAxis.Y, true)
        transition.interpolator = AnticipateOvershootInterpolator()
        transition.duration = 400
        transition.addTarget(R.id.treeBody)
        transition.addTarget(R.id.treeArticle)
        return transition
    }


}