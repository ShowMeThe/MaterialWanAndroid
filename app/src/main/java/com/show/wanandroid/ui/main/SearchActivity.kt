package com.show.wanandroid.ui.main

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.google.android.material.transition.MaterialSharedAxis
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivitySearchBinding
import com.show.wanandroid.ui.main.fragment.SearchArticleFragment
import com.show.wanandroid.ui.main.fragment.SearchBodyFragment
import com.show.wanandroid.ui.main.vm.SearchViewModel
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.activity_search.*
import showmethe.github.core.base.BaseActivity
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbarScreen

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {

    override fun initViewModel(): SearchViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.activity_search

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

        viewModel.searchWord.observe(this, Observer {
            it?.apply {
                if(it.isNotEmpty()){
                    edSearch.setText(it)
                    replaceToArticle()
                }
            }
        })
    }



    override fun init(savedInstanceState: Bundle?) {
        fixToolbarScreen(toolBar)
        binding.main = this
        SkinManager.getInstant().bindings(binding)


        val fragment = SearchBodyFragment()
        fragment.exitTransition = createTransition(true)
        fragment.exitTransition = createTransition(false)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frame,fragment)
            .commitAllowingStateLoss()


    }

    override fun initListener() {

        edSearch.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE){
                val search = edSearch.text.toString().trim()
                if(search.isNotEmpty()){
                    viewModel.searchWord set search
                    replaceToArticle()
                }

            }
            true
        }
    }

    private fun replaceToArticle(){
        val fragment =
            SearchArticleFragment()
        fragment.exitTransition = createTransition(true)
        fragment.exitTransition = createTransition(false)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.frame,fragment)
            .commitAllowingStateLoss()
    }


    private fun createTransition(boolean: Boolean): MaterialSharedAxis? {
        val transition = MaterialSharedAxis.create(context, MaterialSharedAxis.Z, boolean)
        transition.interpolator = AccelerateInterpolator()
        transition.duration = 350
        transition.addTarget(R.id.searchBody)
        return transition
    }

    fun popBackToFront(){
        finishAfterTransition()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.searchWord set ""
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}