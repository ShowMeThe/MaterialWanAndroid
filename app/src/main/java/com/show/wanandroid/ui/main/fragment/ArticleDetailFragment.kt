package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentArticleDetailBinding
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_article_detail.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.glide.load
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar

class ArticleDetailFragment : BaseFragment<FragmentArticleDetailBinding, MainViewModel>() {

    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_article_detail

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {
        viewModel.openWeb.observe(this, Observer {
            it?.apply {
                tvTitle.text = first
                tvTitle.isSelected = true
                tvTitle.isFocusable = true
                tvTitle.isFocusableInTouchMode = true

                webView.loadUrl(second)
                smrl.showContent()
            }
        })

    }

    override fun init(savedInstanceState: Bundle?) {
        fixToolbar(toolBar)
        binding?.main = this



    }

    override fun initListener() {


    }

    fun backPress() {
        viewModel.replace set ""
        requireActivity()
            .supportFragmentManager.popBackStack()
    }

}