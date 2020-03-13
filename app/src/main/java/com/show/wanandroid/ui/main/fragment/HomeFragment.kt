package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import android.util.Log
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
import com.show.wanandroid.entity.Article
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.divider.RecycleViewDivider
import showmethe.github.core.glide.TGlide
import showmethe.github.core.util.extras.ObList
import showmethe.github.core.util.extras.plus
import showmethe.github.core.util.extras.post
import showmethe.github.core.util.extras.valueSameAs

class HomeFragment : LazyFragment<FragmentHomeBinding, MainViewModel>() {


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
                                list.clear()
                            }
                            this.datas?.apply {
                                list.addAll(this)
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

    }

    override fun init() {
        refresh.setColorSchemeResources(R.color.colorAccent)
        rv.hideWhenScrolling(refresh)
        banner.setOnImageLoader { url, imageView ->
            TGlide.loadNoCrop(url, imageView)
        }
        banner.bindToLife(this)

        adapter = ArticleListAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        rv.addItemDecoration(RecycleViewDivider(LinearLayoutManager.VERTICAL,dividerColor = ContextCompat.getColor(context,R.color.colorAccent)))

        router.toTarget("getBanner")

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
                viewModel.homeCollect(item.id)
            }else{
                viewModel.homeUnCollect(item.id)
            }
        }


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