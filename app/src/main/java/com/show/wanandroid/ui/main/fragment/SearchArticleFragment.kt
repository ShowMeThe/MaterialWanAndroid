package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentSearchArticleBinding
import com.show.wanandroid.entity.Article
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_search_article.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.divider.RecycleViewDivider
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.util.extras.ObList
import showmethe.github.core.util.extras.plus
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.extras.valueSameAs

class SearchArticleFragment : BaseFragment<FragmentSearchArticleBinding, MainViewModel>() {

    private var search = ""
    private lateinit var adapter :ArticleListAdapter
    private val list = ObList<Article.DatasBean>()
    private val pager = MutableLiveData<Int>()
    override fun initViewModel(): MainViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_search_article

    override fun onBundle(bundle: Bundle) {


    }

    override fun observerUI() {
        viewModel.search.observe(this, Observer {
            it?.apply {
                when(status){
                    Result.Loading -> refresh.isRefreshing = true
                    Result.Success ->{
                        response?.apply {
                            if(pager valueSameAs 0){
                                list.clear()
                            }
                            refresh.isRefreshing = false
                            list.addAll(datas)
                            onLoadSize(list.size)
                        }
                    }
                }
            }
        })

        viewModel.searchWord.observe(this, Observer {
            it?.apply {
                search = this
                pager set 0
            }
        })

        pager.observe(this, Observer {
            it?.apply {
                router.toTarget("search",it,search)
            }
        })

    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.main = this
        ivBack.startAnimator()

        initAdapter()

    }

    override fun initListener() {

        refresh.setOnRefreshListener {
            pager set 0
        }

        rv.setOnLoadMoreListener {
            pager.plus(1)
        }
    }


    private fun initAdapter(){
        adapter = ArticleListAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        rv.addItemDecoration(RecycleViewDivider(LinearLayoutManager.VERTICAL,dividerColor = ContextCompat.getColor(context,R.color.colorAccent)))
    }



    fun onBackPress(){
        childFragmentManager.popBackStack()
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