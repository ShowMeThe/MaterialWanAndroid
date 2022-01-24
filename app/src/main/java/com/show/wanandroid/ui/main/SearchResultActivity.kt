package com.show.wanandroid.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.base.BaseActivity
import com.show.kcore.base.BundleInject
import com.show.kcore.base.Transition
import com.show.kcore.base.TransitionMode
import com.show.kcore.extras.gobal.read
import com.show.kcore.extras.status.statusBar
import com.show.slideback.annotation.SlideBackBinder
import com.show.wanandroid.R
import com.show.wanandroid.bean.DatasBean
import com.show.wanandroid.databinding.ActivitySearchResultBinding
import com.show.wanandroid.toast
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.ui.main.vm.SearchViewModel

@SlideBackBinder
@Transition(mode = TransitionMode.Fade)
class SearchResultActivity : BaseActivity<ActivitySearchResultBinding,SearchViewModel>() {

    @BundleInject("title")
    var title = ""

    private val mainViewModel by viewModels<MainViewModel> { ViewModelProvider.AndroidViewModelFactory(application) }

    private var page = 0
    private val list = ObservableArrayList<DatasBean>()
    val adapter by lazy { ArticleListAdapter(this, list) }
    val refreshData = MutableLiveData(true)
    val layoutManager by lazy {
        LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
    }


    override fun getViewId(): Int = R.layout.activity_search_result

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        viewModel.search
            .asLiveData()
            .read(this,timeOut = {
            refreshData.value = false
        },error = {exception, t ->
            refreshData.value = false
                t?.apply {
                    toast(errorCode,errorMsg)
                }
        }){
            it?.data?.apply {
                if(page == 0){
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
        statusBar { uiFullScreen(true) }

        binding {

            main = this@SearchResultActivity
            executePendingBindings()



            getData()

        }


    }

    override fun initListener() {
        binding {

            refresh.setOnRefreshListener {
                page = 0
                getData()
            }

            rvList.setOnLoadMoreListener {
                page++
                getData()
            }


            adapter.setOnLikeClickListener { item, isCollect ->
                if(isCollect){
                    mainViewModel.homeCollect(item.id)
                }else{
                    mainViewModel.homeUnCollect(item.id)
                }
            }

            adapter.setOnItemClickListener { view, data, position ->
                WebActivity.start(this@SearchResultActivity,data.title,data.link)
            }

        }
    }

    private fun getData(){
        viewModel.search(page,title)
    }
}