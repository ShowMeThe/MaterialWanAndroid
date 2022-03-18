package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.base.BundleInject
import com.show.kcore.base.LazyFragment
import com.show.kcore.extras.gobal.read
import com.show.kcore.http.coroutines.KResult
import com.show.wanandroid.R
import com.show.wanandroid.bean.Article
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.bean.JsonData
import com.show.wanandroid.const.BundleConst
import com.show.wanandroid.databinding.FragmentArticleBinding
import com.show.wanandroid.ui.main.WebActivity
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.SkinManager
import kotlinx.coroutines.flow.MutableSharedFlow


class ArticleFragment : LazyFragment<FragmentArticleBinding, MainViewModel>() {

    companion object {

        fun get(id: Int): ArticleFragment {
            val bundle = Bundle()
            bundle.putInt(BundleConst.Id, id)
            val fragment = ArticleFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    @BundleInject(BundleConst.Id)
    private var tabId = -1

    private var page = 0
    private val article = MutableSharedFlow<KResult<JsonData<Article>>>()
    private val list = ObservableArrayList<DatasBean>()

    val layoutManager by lazy {
        LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
    }
    val adapter by lazy { ArticleListAdapter(requireContext(), list) }
    val refreshData = MutableLiveData(true)

    override fun getViewId(): Int = R.layout.fragment_article


    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {


        article
            .read(viewLifecycleOwner,
            error = { _,_->
                refreshData.value = false
            },
            timeOut = {
            refreshData.value = false
        }) { jsonData ->
            jsonData?.data?.apply {
                if (page == 0) {
                    list.clear()
                }
                list.addAll(datas)
                refreshData.value = false
                binding {
                    rvList.finishLoading()
                    rvList.setEnableLoadMore(datas.isNotEmpty())
                }
            }
        }
    }


    override fun init(savedInstanceState: Bundle?) {
        binding {
            //refresh.setColorSchemeResources(R.color.colorAccent)

            SkinManager.getManager().autoTheme(SkinManager.currentStyle,binding)

            main = this@ArticleFragment
            executePendingBindings()


            getArticles()

        }


    }

    private fun getArticles() {
        viewModel.getChaptersArticle(tabId, page, article)
    }


    override fun initListener() {

        binding {

            rvList.setOnLoadMoreListener {
                page++
                getArticles()
            }

            refresh.setOnRefreshListener {
                page = 0
                getArticles()
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

}