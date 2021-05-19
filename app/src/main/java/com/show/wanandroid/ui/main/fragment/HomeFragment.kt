package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.base.BaseFragment
import com.show.kcore.extras.gobal.read
import com.show.kcore.glide.TGlide.Companion.load
import com.show.kcore.rden.Stores
import com.show.wanandroid.CONFIG
import com.show.wanandroid.R
import com.show.wanandroid.bannerPlugin
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.databinding.FragmentHomeBinding
import com.show.wanandroid.ui.main.SearchActivity
import com.show.wanandroid.ui.main.WebActivity
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.SkinManager
import com.showmethe.skinlib.getColorExtras
import com.showmethe.skinlib.isStyleFromJson
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

                    val styleName = Stores.getString("theme","BlueTheme")!!
                    if(styleName.isStyleFromJson()){
                        bannerPlugin.individuate(banner,styleName,styleName.getColorExtras())
                    }else{
                        bannerPlugin.individuate(banner,styleName)
                    }
                }
            }
        }

        viewModel.homeTops.read(viewLifecycleOwner,
            error = { e, t ->
                refreshData.value = false
            },
            timeOut = {
                refreshData.value = false
        }){
            it?.apply {
                list.clear()
                list.addAll(this)
                refreshData.value = false
                binding.rvList.finishLoading()
                binding.rvList.setEnableLoadMore(it.isNotEmpty())
            }
        }

        viewModel.homeArticle.read(this){
            it?.data?.apply {
                list.addAll(datas)
                binding.rvList.finishLoading()
                binding.rvList.setEnableLoadMore(list.size != 0)
            }
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        binding {

            initExpand()

            SkinManager.getManager().autoTheme(SkinManager.currentStyle,binding)

            main = this@HomeFragment
            executePendingBindings()

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
                        startActivity<SearchActivity>()
                    }
                    1 ->{
                        rvList.scrollToPosition(0)
                    }
                }
            }

            adapter.setOnLikeClickListener { item, isCollect ->
                if(isCollect){
                    viewModel.homeCollect(item.id)
                }else{
                    viewModel.homeUnCollect(item.id)
                }
            }

            adapter.setOnItemClickListener { view, data, position ->
                WebActivity.start(requireActivity(),data.title,data.link)
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