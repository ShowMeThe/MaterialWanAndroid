package com.show.wanandroid.ui.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.show.kcore.base.BundleInject
import com.show.kcore.base.LazyFragment
import com.show.kcore.extras.gobal.read
import com.show.kcore.extras.log.Logger
import com.show.kcore.http.coroutines.KResultData
import com.show.wanandroid.R
import com.show.wanandroid.bean.CateBean
import com.show.wanandroid.bean.Data
import com.show.wanandroid.bean.JsonData
import com.show.wanandroid.const.BundleConst
import com.show.wanandroid.databinding.FragmentProjectArticleBinding
import com.show.wanandroid.ui.main.WebActivity
import com.show.wanandroid.ui.main.adapter.ProjectAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.SkinManager


class ProjectArticleFragment : LazyFragment<FragmentProjectArticleBinding, MainViewModel>() {

    companion object {

        fun get(id: Int): ProjectArticleFragment {
            val bundle = Bundle()
            bundle.putInt(BundleConst.Id, id)
            val fragment =
                ProjectArticleFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    @BundleInject(BundleConst.Id)
    private var cid = 0

    private var page = 1
    private val articles by lazy { KResultData<JsonData<CateBean>>() }
    private val list = ObservableArrayList<Data>()
    val adapter by lazy { ProjectAdapter(requireContext(),list) }
    val layoutManager by lazy { StaggeredGridLayoutManager(2,RecyclerView.VERTICAL) }
    val refreshData by lazy { MutableLiveData(true) }

    override fun getViewId(): Int = R.layout.fragment_project_article

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {


        articles.read(requireActivity(),timeOut = {
            refreshData.value = false
        },error = { _,_->
            refreshData.value = false
        }){
            refreshData.value = false
            it?.data?.apply {
                if(page == 1){
                    list.clear()
                }
                list.addAll(datas)
                binding.rvList.finishLoading()
                binding.rvList.setEnableLoadMore(datas.isNotEmpty())
            }
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        binding {
            SkinManager.getManager().autoTheme(SkinManager.currentStyle,binding)

            main = this@ProjectArticleFragment
            executePendingBindings()


            getArticle()

        }


    }


    override fun initListener() {

        binding {

            refresh.setOnRefreshListener {
                page = 1
                getArticle()
            }


            rvList.setOnLoadMoreListener {
                page++
                getArticle()
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



    private fun getArticle(){
        viewModel.getCate(page,cid,articles)
    }


}