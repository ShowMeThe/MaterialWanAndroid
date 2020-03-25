package com.show.wanandroid.ui.main

import android.os.Bundle
import android.transition.TransitionInflater
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentSearchBinding
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar

class SearchFragment : BaseFragment<FragmentSearchBinding, MainViewModel>() {

    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_search

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }
    override fun init(savedInstanceState: Bundle?) {

        fixToolbar(toolBar)


    }

    override fun initListener() {



    }


}