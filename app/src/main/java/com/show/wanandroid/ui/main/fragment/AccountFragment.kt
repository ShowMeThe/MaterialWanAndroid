package com.show.wanandroid.ui.main.fragment


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.google.android.material.tabs.TabLayoutMediator
import com.show.kcore.base.BaseFragment
import com.show.kcore.extras.gobal.read
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentAccountBinding
import com.show.wanandroid.ui.main.adapter.TabArticleAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.ReflectEdge
import com.showmethe.skinlib.SkinManager


class AccountFragment : BaseFragment<FragmentAccountBinding, MainViewModel>() {

    private val fragments = ArrayList<Fragment>()
    private val adapter by lazy { TabArticleAdapter(fragments, childFragmentManager, lifecycle) }
    private val titles = ArrayList<String>()


    override fun getViewId(): Int = R.layout.fragment_account


    override fun onBundle(bundle: Bundle) {
    }


    override fun observerUI() {

        viewModel.tabs.asLiveData().read(viewLifecycleOwner) { jsonData ->
            jsonData?.data?.apply {
                titles.clear()
                fragments.clear()
                forEach {
                    titles.add(it.name)
                    fragments.add(ArticleFragment.get(it.id))
                }
                initVp()
            }
        }


    }


    override fun init(savedInstanceState: Bundle?) {


        SkinManager.getManager().autoTheme(SkinManager.currentStyle, binding)
        viewModel.getChapters()
    }

    override fun initListener() {


    }


    private fun initVp() {
        binding {

            vp2.adapter = adapter
            vp2.offscreenPageLimit = fragments.size
            TabLayoutMediator(
                tabLayout, vp2
            ) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }

    }


}