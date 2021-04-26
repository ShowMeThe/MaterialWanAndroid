package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.base.BaseFragment
import com.show.kcore.extras.gobal.read
import com.show.kcore.glide.TGlide.Companion.load
import com.show.wanandroid.CONFIG
import com.show.wanandroid.R
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.databinding.FragmentHomeBinding
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, MainViewModel>() {



    private val list = ObservableArrayList<DatasBean>()
    val adapter by lazy { ArticleListAdapter(requireContext(),list) }
    val layoutManager by lazy { LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false) }
    val refreshData = MutableLiveData<Boolean>(true)

    override fun getViewId(): Int = R.layout.fragment_home

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.banner.read(this){
            it?.data?.apply {
                val urls = this.map { it.imagePath }
                binding {
                    banner.addList(ArrayList(urls))
                }
            }
        }

        viewModel.homeTops.read(this){
            it?.apply {
                list.clear()
                list.addAll(this)
                refreshData.value = false
            }
        }

    }

    override fun init(savedInstanceState: Bundle?) {

        binding {
            main = this@HomeFragment
            executePendingBindings()
            refresh.setColorSchemeResources(R.color.colorAccent)
            banner.bindToLife(viewLifecycleOwner)
            banner.setOnImageLoader { url, imageView ->
                imageView.load(url,CONFIG)
            }

        }


        getBanner()
        getHomeTop()

    }



    override fun initListener() {

        binding{

            refresh.setOnRefreshListener {
                getHomeTop()
            }

            rvList.setOnLoadMoreListener {



            }


        }

    }


    private fun getHomeTop() {
        viewModel.getHomeTop()
    }
    private fun getBanner(){
        viewModel.getBanner()
    }


}