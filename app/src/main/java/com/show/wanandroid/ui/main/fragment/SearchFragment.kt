package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.google.android.material.transition.MaterialSharedAxis
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentSearchBinding
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.fragment_search.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.system.openKeyboard
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
        binding?.main = this



        val fragment = SearchBodyFragment()
        fragment.enterTransition = createTransition()
        childFragmentManager.beginTransaction()
            .replace(R.id.frame,fragment)
            .commitAllowingStateLoss()


    }

    override fun initListener() {

        edSearch.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE){
                viewModel.searchWord set edSearch.text.toString().trim()
            }
            true
        }




    }


    private fun createTransition(): MaterialSharedAxis? {
        val transition = MaterialSharedAxis.create(context, MaterialSharedAxis.Z, true)
        transition.interpolator = AccelerateInterpolator()
        transition.duration = 350
        transition.addTarget(R.id.searchBody)
        return transition
    }

    fun popBackToFront(){
        requireActivity()
            .supportFragmentManager
            .popBackStack()
    }

}