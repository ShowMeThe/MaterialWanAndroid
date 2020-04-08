package com.show.wanandroid.ui.article.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.wanandroid.R
import com.show.wanandroid.const.Id
import com.show.wanandroid.databinding.FragmentArticleBinding
import com.show.wanandroid.entity.Article
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_article.refresh
import kotlinx.android.synthetic.main.fragment_article.rv
import kotlinx.android.synthetic.main.fragment_home.*
import showmethe.github.core.adapter.BaseRecyclerViewAdapter

import showmethe.github.core.base.LazyFragment
import showmethe.github.core.divider.RecycleViewDivider
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.util.extras.plus
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.extras.valueIsNull
import showmethe.github.core.util.extras.valueSameAs


class ArticleFragment : LazyFragment<FragmentArticleBinding, MainViewModel>() {

    companion object {

        fun get(id: Int): ArticleFragment {
            val bundle = Bundle()
            bundle.putInt(Id, id)
            val fragment = ArticleFragment()
            fragment.arguments = bundle
            return fragment
        }

    }


    private val list = ObservableArrayList<Article.DatasBean>()
    private lateinit var adapter: ArticleListAdapter
    private val pagerNumber = MutableLiveData<Int>()
    private val article = MutableLiveData<Result<Article>>()
    private var accountId = 0

    override fun initViewModel(): MainViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_article


    override fun onBundle(bundle: Bundle) {
        accountId = bundle.getInt(Id, 0)

    }

    override fun observerUI() {

        pagerNumber.observe(this, Observer {
            it?.apply {
                router.toTarget("getArticle", accountId, this, article)
            }
        })


        article.observe(this, Observer {
            it?.apply {
                when (status) {
                    Result.Success -> {
                        response?.apply {
                            if (pagerNumber valueSameAs 0) {
                                list.clear()
                            }
                            list.addAll(datas)
                            if (list.size != 0) {
                                smrl.showContent()
                            } else {
                                smrl.showEmpty()
                            }
                            onSize(datas.size)
                        }
                    }
                    Result.OutTime -> {
                        refresh.isRefreshing = false
                        pagerNumber set 0
                    }
                }

            }
        })


    }

    override fun init(savedInstanceState: Bundle?) {
        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)

        initAdapter()

        if (article.valueIsNull()) {
            pagerNumber set 0
        }
    }


    private fun initAdapter() {
        adapter = ArticleListAdapter(context, list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      //  rv.addItemDecoration(RecycleViewDivider(LinearLayoutManager.VERTICAL,dividerColor = ContextCompat.getColor(context,R.color.colorAccent)))
    }


    override fun initListener() {

        smrl.setOnReloadWhenErrorOrEmpty {
            pagerNumber set 0
        }


        refresh.setOnRefreshListener {

            pagerNumber set 0
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

    }


    private fun onSize(size: Int) {
        rv.finishLoading()
        refresh.isRefreshing = false
        if (size == 0) {
            rv.setEnableLoadMore(false)
        } else {
            rv.setEnableLoadMore(true)
        }
    }


}