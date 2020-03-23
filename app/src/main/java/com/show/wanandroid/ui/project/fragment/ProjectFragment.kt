package com.show.wanandroid.ui.project.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentProjectBinding
import com.show.wanandroid.entity.TabItem
import com.show.wanandroid.ui.article.adapter.TabArticleAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_project.*
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.http.coroutines.Result

/**
 *  com.show.wanandroid.ui.project
 *  2020/3/23
 *  23:34
 */
class ProjectFragment : LazyFragment<FragmentProjectBinding, MainViewModel>() {

    private lateinit var adapter : TabArticleAdapter
    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<TabItem>()
    override fun initViewModel(): MainViewModel  = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_project

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

        viewModel.cateTab.observe(this, Observer {
            it?.apply {
                when(status){
                    Result.Success ->{
                        response?.apply {
                            titles.clear()
                            fragments.clear()
                            forEach { tab ->
                                titles.add(TabItem(tab.id,tab.name))
                                fragments.add(ProjectArticleFragment.get(tab.id))
                            }
                            initVp()
                        }
                    }
                }
            }
        })

    }

    override fun init() {


        router.toTarget("getCateTab")


    }



    override fun initListener() {


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
                positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {

            }

        })


    }

}