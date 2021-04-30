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
import com.showmethe.speeddiallib.expand.ExpandIcon
import com.showmethe.speeddiallib.expand.ExpandManager

class HomeFragment : BaseFragment<FragmentHomeBinding, MainViewModel>() {



    private var page = 0
    private val list = ObservableArrayList<DatasBean>()
    val adapter by lazy { ArticleListAdapter(requireContext(),list) }
    val layoutManager by lazy { LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false) }
    val refreshData = MutableLiveData<Boolean>(true)

    override fun getViewId(): Int = R.layout.fragment_home

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.banner.read(viewLifecycleOwner){
            it?.data?.apply {
                val urls = this.map { it.imagePath }
                binding {
                    banner.addList(ArrayList(urls))
                }
            }
        }

        viewModel.homeTops.read(viewLifecycleOwner){
            it?.apply {
                list.clear()
                list.addAll(this)
                refreshData.value = false
                binding.rvList.finishLoading()
                binding.rvList.setEnableLoadMore(list.size != 0)
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

        initExpand()


        getBanner()
        getHomeTop()

    }



    override fun initListener() {

        binding{

            refresh.setOnRefreshListener {
                page = 0
                getHomeTop()
            }

            rvList.setOnLoadMoreListener {
                page++
                getHomeArticle()
            }


            crl.setOnMenuClickListener {
                when(it){
                    0 ->{
                        rvList.scrollToPosition(0)
                    }
                    1 ->{

                    }
                }
            }

        }
    }


    private fun initExpand(){
        val expands = ArrayList<ExpandIcon>()
        expands.add(ExpandIcon().setIcon(R.drawable.ic_arrow_up).setBackgroundTint(R.color.colorPrimaryDark))
        expands.add(ExpandIcon().setIcon(R.drawable.ic_search).setBackgroundTint(R.color.colorPrimaryDark))
        ExpandManager.newBuilder().setExpandIcons(expands).motion(R.color.black,R.drawable.ic_close)
            .bindTarget(binding.crl).build()
    }

    private fun getHomeArticle(){
        viewModel.getHomeArticle(page)
    }


    private fun getHomeTop() {
        viewModel.getHomeTop()
    }
    private fun getBanner(){
        viewModel.getBanner()
    }


}