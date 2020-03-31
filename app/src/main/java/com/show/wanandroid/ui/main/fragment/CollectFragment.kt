package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentCollectBinding
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_collect.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar

class CollectFragment : BaseFragment<FragmentCollectBinding, MainViewModel>() {

    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_collect

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {
        fixToolbar(toolBar)
        binding?.main = this

    }

    override fun initListener() {


    }


    fun backPress(){
        requireActivity()
            .supportFragmentManager.popBackStack()
    }
}