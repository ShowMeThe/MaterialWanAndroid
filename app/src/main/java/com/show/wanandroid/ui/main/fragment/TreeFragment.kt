package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import com.google.android.material.transition.MaterialSharedAxis
import com.show.kcore.base.BaseFragment
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentTreeBinding
import com.show.wanandroid.getShareViewModel
import com.show.wanandroid.replaceFragment
import com.show.wanandroid.ui.main.vm.TreeViewModel

class TreeFragment : BaseFragment<FragmentTreeBinding, TreeViewModel>() {

    private val fragments by lazy { arrayListOf(TreeBodyFragment()) }

    override fun getViewId(): Int = R.layout.fragment_tree

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

        viewModel.navigator.observe(this){
            if(it == null){
                childFragmentManager.popBackStack()
                getShareViewModel().popBack.value = 1
            }else{
                replaceFragment(TreeArticleFragment(),
                    transition = createTransition())
                getShareViewModel().popBack.value = 2
            }
        }

        getShareViewModel().popBack.observe(this){
            if(it == null && childFragmentManager.fragments.size == 2){
                childFragmentManager.popBackStack()
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {


        replaceFragment(fragments[0],transition = createTransition())

    }

    override fun initListener() {


    }

    private fun createTransition(): androidx.transition.TransitionSet {
        val transitionSet = androidx.transition.TransitionSet()
        val transition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        transition.interpolator = LinearInterpolator()
        transition.duration = 300
        transition.addTarget(R.id.smart)
        transition.addTarget(R.id.body)
        transitionSet.addTransition(transition)
        transitionSet.addTransition(androidx.transition.Fade())
        return transitionSet
    }

}