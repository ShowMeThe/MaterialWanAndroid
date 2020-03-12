package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentHomeBinding
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import showmethe.github.core.http.coroutines.Result
import androidx.lifecycle.Observer
import showmethe.github.core.base.LazyFragment

class HomeFragment : LazyFragment<FragmentHomeBinding, MainViewModel>() {


    private  val bannerList  = ArrayList<String>()
    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_home

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.banner.observe(this, Observer { bean ->
            bean?.apply {
                if(status == Result.Success){
                    response?.apply {
                        bannerList.clear()
                        forEach {
                            bannerList.add(it.imagePath)
                        }
                        banner.addList(bannerList)
                    }
                }
            }
        })


    }

    override fun init() {
        router.toTarget("getBanner")



    }

    override fun initListener() {
    }
}