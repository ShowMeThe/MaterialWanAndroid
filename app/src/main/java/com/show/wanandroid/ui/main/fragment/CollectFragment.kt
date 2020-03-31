package com.show.wanandroid.ui.main.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentCollectBinding
import com.show.wanandroid.entity.Collect
import com.show.wanandroid.themes_name
import com.show.wanandroid.ui.main.adapter.CollectAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.fragment_collect.*
import showmethe.github.core.adapter.slideAdapter.SlideAdapter
import showmethe.github.core.adapter.slideAdapter.SlideCreator
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.divider.RecycleViewDivider
import showmethe.github.core.util.extras.ObList
import showmethe.github.core.util.extras.plus
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.extras.valueSameAs
import showmethe.github.core.util.widget.StatusBarUtil.fixToolbar

class CollectFragment : BaseFragment<FragmentCollectBinding, MainViewModel>() {

    private val pager = MutableLiveData<Int>()
    private lateinit var adapter: CollectAdapter
    private val list = ObList<Collect.DatasBean>()

    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_collect

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {

        pager.observe(this, Observer {
            it?.apply {
                router.toTarget("getCollect", this)
            }
        })

        viewModel.collect.observe(this, Observer {
            it?.apply {
                if(pager valueSameAs  0){
                    list.clear()
                }
                response?.apply {
                    list.addAll(datas)
                    onLoadSize(datas.size)
                }
            }
        })

    }

    override fun init(savedInstanceState: Bundle?) {
        fixToolbar(toolBar)
        binding?.main = this

        initAdapter()


        pager set 0
    }

    override fun initListener() {

        rv.setOnLoadMoreListener {
            pager.plus(1)
        }

        refresh.setOnRefreshListener {
            pager set 0
        }

        adapter.setOnSlideClickListener(object : SlideAdapter.OnSlideClickListener{
            override fun onMenuItemClick(contentPos: Int, menuPosition: Int) {
                val item = list[contentPos]
                router.toTarget("unCollect",item.id,item.originId)
                list.removeAt(contentPos)
            }

            override fun onContentItemClick(position: Int) {

            }

        })

    }

    private fun initAdapter() {
        adapter = CollectAdapter(context, list)
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv.adapter = adapter
        rv.hideWhenScrolling(refresh)
        val color = when (SkinManager.currentStyle) {
            themes_name[0] -> R.color.colorAccent
            themes_name[1] -> R.color.color_304ffe
            themes_name[2] -> R.color.color_6200ea
            else -> R.color.colorAccent
        }
        SlideCreator().addItemMenu(getString(R.string.cancel_collect), R.color.white, color, 15f)
            .bind(adapter, 0.2f)
    }

    fun backPress() {
        requireActivity()
            .supportFragmentManager.popBackStack()
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