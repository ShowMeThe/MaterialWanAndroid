package com.show.wanandroid.ui.main

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.kcore.base.BaseActivity
import com.show.kcore.base.Transition
import com.show.kcore.base.TransitionMode
import com.show.kcore.extras.counter.timer
import com.show.kcore.extras.display.dp
import com.show.kcore.extras.gobal.read
import com.show.kcore.extras.status.statusBar
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import com.show.wanandroid.R
import com.show.wanandroid.bean.Collect
import com.show.wanandroid.databinding.ActivityCollectBinding
import com.show.wanandroid.toast
import com.show.wanandroid.ui.main.adapter.CollectAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.widget.swipe.Menu
import com.show.wanandroid.widget.swipe.SwipeBuilder
import com.show.wanandroid.widget.swipe.TextMenu
import com.showmethe.skinlib.SkinManager
import kotlinx.coroutines.Dispatchers

@SlideBackPreview
@SlideBackBinder
@Transition(mode = TransitionMode.SlideStart)
class CollectActivity : BaseActivity<ActivityCollectBinding, MainViewModel>() {


    private var page = 0
    private val list by lazy { ObservableArrayList<Collect.DatasBean>() }
    private val menus by lazy {
        val builder = SwipeBuilder.builder()
        builder.addMenu(TextMenu {
            text = "取消收藏"
            backgroundColor = Color.RED
            textColor = Color.WHITE
            drawableStart = ContextCompat.getDrawable(this@CollectActivity,R.drawable.ic_hidden)
            menuWidth = 130f.dp
            drawablePadding = 0f.dp
            menuPadding = 5f.dp
        }).build()
    }
    var refreshData = MutableLiveData(true)
    val layoutManager by lazy { LinearLayoutManager(this, RecyclerView.VERTICAL, false) }

    val adapter by lazy { CollectAdapter(this,menus,list) }

    override fun getViewId(): Int = R.layout.activity_collect

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {
        viewModel.collects.read(this, timeOut = {
            refreshData.value = false
        }, error = { _, it ->
            it?.apply {
                toast(errorCode,errorMsg)
            }
            refreshData.value = false
        }){
            it?.data?.apply {
                if(page == 0){
                    list.clear()
                    adapter.clearRestoreState()
                }
                list.addAll(datas)
                binding {
                    refreshData.value = false
                    rvList.finishLoading()
                    rvList.setEnableLoadMore(datas.isNotEmpty())
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        statusBar {
            uiFullScreen(true)
        }
        binding {
            main = this@CollectActivity
            executePendingBindings()

            SkinManager.getManager().autoTheme(SkinManager.currentStyle, this)

            rvList.hideWhenScrolling(refresh)
        }

        timer(Dispatchers.Main,300){
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


            adapter.setOnItemClickListener { view, data, position ->
                WebActivity.start(this@CollectActivity,data.title,data.link)
            }

            adapter.setOnMenuItemClickListener { menuPosition, contentPosition ->
                if(menuPosition == 0){
                    val item = list[contentPosition]
                    viewModel.unCollect(item.id,item.originId)
                    adapter.removeRestoreState(contentPosition)
                    list.removeAt(contentPosition)
                }
            }

        }
    }

    private fun getData(){
        viewModel.getCollects(page)
    }

}