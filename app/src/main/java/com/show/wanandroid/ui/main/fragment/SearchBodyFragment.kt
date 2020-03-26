package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.Observer
import com.google.android.material.transition.MaterialSharedAxis
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentSearchBodyBinding
import com.show.wanandroid.ui.main.vm.MainViewModel
import showmethe.github.core.base.BaseFragment

/**
 *  com.show.wanandroid.ui.main.fragment
 *  2020/3/26
 *  23:57
 */
class SearchBodyFragment : BaseFragment<FragmentSearchBodyBinding, MainViewModel>() {


    override fun initViewModel(): MainViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_search_body
    override fun onBundle(bundle: Bundle) {

    }


    override fun observerUI() {
        viewModel.hotKey.observe(this, Observer {
            it?.apply {

            }
        })

    }

    override fun init(savedInstanceState: Bundle?) {

        if(viewModel.hotKey.value == null){
            router.toTarget("getHotKey")
        }
    }


    override fun initListener() {

    }
}