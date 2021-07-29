package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.base.BaseFragment
import com.show.kcore.extras.gobal.read
import com.show.kcore.http.coroutines.KResult
import com.show.wanandroid.R
import com.show.wanandroid.bean.Article
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.bean.JsonData
import com.show.wanandroid.databinding.FragmentTreeArticleBinding
import com.show.wanandroid.ui.main.WebActivity
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.ui.main.vm.TreeViewModel
import com.showmethe.skinlib.SkinManager
import kotlinx.coroutines.flow.MutableSharedFlow

class TreeArticleFragment : BaseFragment<FragmentTreeArticleBinding, TreeViewModel>() {


    private var page = 0
    private var articleId = 0
    private val list = ObservableArrayList<DatasBean>()
    private val treeArticle by lazy { MutableSharedFlow<KResult<JsonData<Article>>>() }

    val adapter by lazy { ArticleListAdapter(requireContext(), list) }
    val layoutManager by lazy {
        LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
    }
    val refreshData = MutableLiveData(true)


    override fun getViewId(): Int = R.layout.fragment_tree_article

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.navigator
            .asLiveData()
            .observe(this) {
            it?.apply {
                binding {
                    tvTitle.text = second
                    articleId = first
                    page = 0
                    getArticle()
                }
            }
        }

        treeArticle
            .asLiveData()
            .read(this) {
            it?.data?.apply {
                if (page == 0) {
                    list.clear()
                }
                list.addAll(datas)
                refreshData.value = false
                binding.rvList.finishLoading()
                binding.rvList.setEnableLoadMore(datas.isNotEmpty())
            }
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        binding {
            SkinManager.getManager().autoTheme(SkinManager.currentStyle, binding)
            main = this@TreeArticleFragment
            executePendingBindings()

            //refresh.setColorSchemeResources(R.color.colorAccent)

        }
    }

    override fun initListener() {
        binding {

            rvList.setOnLoadMoreListener {
                page++
                getArticle()
            }

            refresh.setOnRefreshListener {
                page = 0
                getArticle()
            }


            adapter.setOnItemClickListener { view, data, position ->
                WebActivity.start(requireActivity(), data.title, data.link)
            }
        }
    }

    private fun getArticle() {
        viewModel.getTreeArticle(articleId, page, treeArticle)
    }

    fun popBack() {
        viewModel.navigator.tryEmit(null)
    }

}