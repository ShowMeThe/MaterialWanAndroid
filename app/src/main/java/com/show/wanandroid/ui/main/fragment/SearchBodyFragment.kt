package com.show.wanandroid.ui.main.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialSharedAxis
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentSearchBodyBinding
import com.show.wanandroid.entity.KeyWord
import com.show.wanandroid.themes_name
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.util.Util.clearRecently
import com.show.wanandroid.util.Util.getSaveRecently
import com.show.wanandroid.util.Util.saveRecently
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.fragment_search_body.*
import kotlinx.android.synthetic.main.fragment_search_body.group
import kotlinx.android.synthetic.main.item_tree_body.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.set

/**
 *  com.show.wanandroid.ui.main.fragment
 *  2020/3/26
 *  23:57
 */
class SearchBodyFragment : BaseFragment<FragmentSearchBodyBinding, MainViewModel>() {


    private var needRefresh = true
    override fun initViewModel(): MainViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_search_body
    override fun onBundle(bundle: Bundle) {

    }


    override fun observerUI() {

        viewModel.hotKey.observe(this, Observer {
            it?.apply {
                response?.apply {
                    addGroup(this)
                    SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)
                }
            }
        })

        viewModel.searchWord.observe(this, Observer {
            it?.apply {
                needRefresh = true
                saveRecently()
                addRecently()
            }
        })

    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.main = this
        if(viewModel.hotKey.value == null){
            router.toTarget("getHotKey")
        }


    }


    override fun initListener() {


    }

    fun clearAll(){
        reGroup.removeAllViews()
        clearRecently()
    }

    override fun onVisible() {
        addRecently()
    }

    private fun addGroup(array:ArrayList<KeyWord>){
        array.forEach { text ->
            val chip = View.inflate(context,R.layout.chip_tree_layout,null) as Chip
            chip.text =  text.name
            chip.setTextColor(Color.WHITE)
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#5f4fc3f7"))
            group.addView(chip)
            chip.setOnClickListener {
                viewModel.searchWord set text.name
            }
        }
    }



    private fun addRecently(){
        if(!needRefresh) return
        val array = getSaveRecently()
        reGroup.removeAllViews()
        array.forEach { text ->
            val chip = View.inflate(context,R.layout.chip_tree_layout,null) as Chip
            chip.text =  text
            chip.setTextColor(Color.WHITE)
            chip.chipBackgroundColor = when(SkinManager.currentStyle){
                themes_name[0] -> ColorStateList.valueOf(Color.parseColor("#5f4fc3f7"))
                themes_name[1] -> ColorStateList.valueOf(Color.parseColor("#5fff4081"))
                themes_name[2] -> ColorStateList.valueOf(Color.parseColor("#5f7c4dff"))
                else -> ColorStateList.valueOf(Color.parseColor("#5f4fc3f7"))
            }
            chip.setOnClickListener {
                viewModel.searchWord set text
            }
            reGroup.addView(chip)
        }
        needRefresh  = false
    }

}