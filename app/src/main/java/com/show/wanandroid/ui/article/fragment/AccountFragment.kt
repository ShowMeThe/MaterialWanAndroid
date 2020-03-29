package com.show.wanandroid.ui.article.fragment




import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentAccuntBinding
import com.show.wanandroid.entity.TabItem
import com.show.wanandroid.ui.article.adapter.TabArticleAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.SkinManager

import kotlinx.android.synthetic.main.fragment_accunt.*
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.util.extras.onTabSelected
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar


class AccountFragment : LazyFragment<FragmentAccuntBinding, MainViewModel>() {

    private lateinit var adapter : TabArticleAdapter
    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<TabItem>()

    override fun initViewModel(): MainViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_accunt



    override fun onBundle(bundle: Bundle) {
    }



    override fun observerUI() {

        viewModel.tabs.observe(this, Observer {
            it?.apply {
                when(status){
                    Result.Success ->{
                        response?.apply {
                            titles.clear()
                            fragments.clear()
                            forEach { tab ->
                                titles.add(TabItem(tab.id,tab.name))
                                fragments.add(ArticleFragment.get(tab.id))
                            }
                            initVp()
                        }
                    }
                    Result.OutTime ->{
                        router.toTarget("getChapters")
                    }
                }
            }
        })



    }


    override fun init() {
        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)

        router.toTarget("getChapters")

    }

    override fun initListener() {

        tab.onTabSelected {
            onTabSelected {

            }
        }


    }


    private fun initVp(){
        adapter = TabArticleAdapter(fragments,titles,childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        vp.adapter = adapter
        vp.offscreenPageLimit = fragments.size
        tab.setupWithViewPager(vp)

        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

        })


    }


}