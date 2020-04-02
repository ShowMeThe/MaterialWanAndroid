package com.show.wanandroid.ui.main.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentHomeBinding
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import showmethe.github.core.http.coroutines.Result
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.show.wanandroid.app.AppApplication.Companion.bannerPlugin
import com.show.wanandroid.entity.Article
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.widget.IconSwitch
import com.showmethe.skinlib.SkinManager
import com.showmethe.speeddiallib.expand.ExpandIcon
import com.showmethe.speeddiallib.expand.ExpandManager
import showmethe.github.core.adapter.BaseRecyclerViewAdapter
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.divider.RecycleViewDivider
import showmethe.github.core.glide.TGlide
import showmethe.github.core.util.extras.*
import showmethe.github.core.util.rden.RDEN
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar

class HomeFragment : LazyFragment<FragmentHomeBinding, MainViewModel>() {


    private var topSize = 0
    private val pagerNumber = MutableLiveData<Int>()
    private lateinit var adapter: ArticleListAdapter
    private val list = ObList<Article.DatasBean>()
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
                        /**
                         * Banner需要获得数据长度才会新建dot的
                         */
                        bannerPlugin.individuate(banner,RDEN.get("theme",""))
                    }
                }
            }
        })

        pagerNumber.observe(this, Observer {
            it?.apply {
                router.toTarget("getHomeArticle",this)
            }
        })

        viewModel.article.observe(this, Observer {
            it?.apply {
                when(status){
                    Result.Success ->{
                        response?.apply {
                            if(pagerNumber valueSameAs  1){
                                if(topSize!=0){
                                    list.clearAfter(topSize - 1)
                                }else{
                                    list.clear()
                                }
                            }
                            this.datas.apply {
                                if(topSize == 0){
                                    list.addAll(this)
                                }else{
                                    list.addAll(topSize,this)
                                }
                                onLoadSize(size)
                            }
                        }
                    }
                    Result.OutTime -> {  //增加一处超时
                        rv.finishLoading()
                        refresh.isRefreshing = false
                    }
                }
            }
        })

        viewModel.tops.observe(this, Observer {
            it?.apply {
                when(status){
                    Result.Success ->{
                        response?.apply {
                            topSize = size
                            forEach { bean ->
                                bean.isTop = true
                                list.add(0,bean)
                            }
                        }
                    }
                }
            }
        })



    }

    override fun init() {
        refresh.setColorSchemeResources(R.color.colorAccent)
        initExpand()
        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)


        initBanner()
        initAdapter()

        router.toTarget("getBanner")
        router.toTarget("getHomeTop")
        pagerNumber post 0
    }


    override fun initListener() {

        refresh.setOnRefreshListener {
            pagerNumber post 0
        }


        rv.setOnLoadMoreListener {
            pagerNumber plus 1
        }


        adapter.setOnLikeClickListener { item, isCollect ->
            if(isCollect){
                router.toTarget("homeCollect",item.id)
            }else{
                router.toTarget("homeUnCollect",item.id)
            }
        }

        adapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val item = list[position]
                viewModel.openWeb set (item.title to item.link)
                viewModel.replace set getString(R.string.transition_name_web)
            }
        })


        crl.setOnMenuClickListener {
            when(it){
                0 ->{
                    rv.scrollToPosition(0)
                }
                1 ->{

                }
            }
        }

    }

    private fun initBanner(){

        banner.setOnImageLoader { url, imageView ->
            TGlide.loadNoCrop(url, imageView)
        }
        banner.bindToLife(this)
    }

    private fun initExpand(){
        val expands = ArrayList<ExpandIcon>()
        expands.add(ExpandIcon().setIcon(R.drawable.ic_arrow_up).setBackgroundTint(R.color.colorPrimaryDark))
        expands.add(ExpandIcon().setIcon(R.drawable.ic_search).setBackgroundTint(R.color.colorPrimaryDark))
        ExpandManager.newBuilder().setExpandIcons(expands).motion(R.color.black,R.drawable.ic_close)
            .bindTarget(crl).build()
    }

    private fun initAdapter(){
        adapter = ArticleListAdapter(context,list)
        rv.adapter = adapter

        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        //rv.addItemDecoration(RecycleViewDivider(LinearLayoutManager.VERTICAL,dividerColor = ContextCompat.getColor(context,R.color.colorAccent)))
    }


    private fun onLoadSize(size: Int) {
        rv.finishLoading()
        refresh.isRefreshing = false
        if(size == 0){
            rv.setEnableLoadMore(false)
        }else{
            rv.setEnableLoadMore(true)
        }
    }
}