package com.show.wanandroid.ui.project.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayoutMediator
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentProjectBinding
import com.show.wanandroid.entity.TabItem
import com.show.wanandroid.ui.article.adapter.TabArticleAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.SkinManager
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
    private val titles = ArrayList<String>()
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
                                titles.add(tab.name)
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

        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)

        router.toTarget("getCateTab")


    }



    override fun initListener() {


    }

    private fun initVp(){
        adapter = TabArticleAdapter(fragments,childFragmentManager,lifecycle)
        vp.adapter = adapter
        vp.offscreenPageLimit = fragments.size
        TabLayoutMediator(tab,vp,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = titles[position]
            }).attach()

    }

}