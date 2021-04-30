package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.show.kcore.base.BaseFragment
import com.show.kcore.extras.gobal.read
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentProjectBinding
import com.show.wanandroid.ui.main.adapter.TabArticleAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel


class ProjectFragment : BaseFragment<FragmentProjectBinding, MainViewModel>() {

    private val fragments = ArrayList<Fragment>()
    private val adapter by lazy { TabArticleAdapter(fragments, childFragmentManager, lifecycle) }
    private val titles = ArrayList<String>()

    override fun getViewId(): Int = R.layout.fragment_project

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

        viewModel.cateTab.read(viewLifecycleOwner){
            it?.data?.apply {
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

    override fun init(savedInstanceState: Bundle?) {



        viewModel.getCateTab()

    }



    override fun initListener() {


    }

    private fun initVp(){
        binding {
            vp.adapter = adapter
            vp.offscreenPageLimit = fragments.size
            TabLayoutMediator(tab,vp
            ) { tab, position ->
                tab.text = titles[position]
            }.attach()

        }

    }

}