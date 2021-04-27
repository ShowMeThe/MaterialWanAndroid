package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import com.show.kcore.base.BaseFragment
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentTreeArticleBinding
import com.show.wanandroid.databinding.FragmentTreeBodyBinding
import com.show.wanandroid.getShareViewModel
import com.show.wanandroid.ui.main.vm.TreeViewModel

class TreeArticleFragment : BaseFragment<FragmentTreeArticleBinding, TreeViewModel>() {

    override fun getViewId(): Int = R.layout.fragment_tree_article

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.navigator.observe(this){
            it?.apply {
                binding{
                    tvTitle.text = second

                }
            }
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        binding {
            main = this@TreeArticleFragment
            executePendingBindings()
        }
    }

    override fun initListener() {

    }


    fun popBack(){
        viewModel.navigator.value = null
    }

}