package com.show.kcore.extras.gobal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.show.kcore.base.AppContext
import com.show.kcore.http.coroutines.KResult


inline fun <reified T : ViewModel> Class<T>.getAppViewModel() = ViewModelProvider(
    AppContext.get().context.applicationContext as ViewModelStoreOwner,
    ViewModelProvider.AndroidViewModelFactory(AppContext.get().context.applicationContext as Application)).get(this)

fun <T> MutableLiveData<KResult<T>>.read(lifecycleOwner:LifecycleOwner,
                                         loading:(()->Unit)? = null,
                                         timeOut :(()->Unit)? = null,
                                         error:(Exception?.()->Unit)? = null,
                                         data:((data:T?)->Unit)? = null,){



    this.observe(lifecycleOwner, Observer {
        it?.apply {
            when(status){
                KResult.Loading ->{
                    loading?.invoke()
                }
                KResult.Success ->{
                    data?.invoke(response)
                }
                KResult.Failure ->{
                    error?.invoke(exception)
                }
                KResult.TimeOut ->{
                    timeOut?.invoke()
                }
            }
        }
    })
}



inline fun <T : View> T.doOnGlobalLayout(crossinline  onLayout: T.()->Unit){
    if (viewTreeObserver.isAlive) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                onLayout()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }
}



inline fun View.setOnSingleClickListener(intervalTime:Long = 450L,crossinline onSingleClick : (it: View)->Unit){
    var lastClickTime = 0L

    setOnClickListener {
        val time = System.currentTimeMillis()
        if(time - lastClickTime > intervalTime){
            onSingleClick.invoke(it)
            lastClickTime = time
        }
    }
}


inline fun TabLayout.onTabSelected(tabSelect: TabSelect.() -> Unit) {
    tabSelect.invoke(TabSelect(this))
}

class TabSelect(tab: TabLayout) {
    private var tabReselected: ((tab: TabLayout.Tab) -> Unit)? = null
    private var tabUnselected: ((tab: TabLayout.Tab) -> Unit)? = null
    private var tabSelected: ((tab: TabLayout.Tab) -> Unit)? = null

    fun onTabReselected(tabReselected: (TabLayout.Tab.() -> Unit)) {
        this.tabReselected = tabReselected
    }

    fun onTabUnselected(tabUnselected: (TabLayout.Tab.() -> Unit)) {
        this.tabUnselected = tabUnselected
    }

    fun onTabSelected(tabSelected: (TabLayout.Tab.() -> Unit)) {
        this.tabSelected = tabSelected
    }

    init {
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.apply { tabReselected?.invoke(tab) }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.apply { tabUnselected?.invoke(tab) }

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.apply { tabSelected?.invoke(tab) }
            }

        })
    }

}


open class SimpleLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }
}

fun View.gone(){
    visibility = View.GONE
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.inVisible(){
    visibility = View.INVISIBLE
}


fun RecyclerView.scrollToPositionExactly(position: Int,smooth:Boolean = true) {
    val findView = layoutManager!!.findViewByPosition(position)
    val orientation = when (layoutManager) {
        is LinearLayoutManager -> {
            (layoutManager as LinearLayoutManager).orientation
        }
        is GridLayoutManager -> {
            (layoutManager as GridLayoutManager).orientation
        }
        is StaggeredGridLayoutManager ->{
            (layoutManager as StaggeredGridLayoutManager).orientation
        }
        else -> {
            RecyclerView.VERTICAL
        }
    }
    if (findView == null) {
        scrollToPosition(position)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when (val layoutManager = layoutManager) {
                    is LinearLayoutManager,is GridLayoutManager,is StaggeredGridLayoutManager -> {
                        val targetView = layoutManager.findViewByPosition(position)
                        if (orientation == RecyclerView.VERTICAL && targetView != null) {
                            if(smooth){
                                smoothScrollBy(0, targetView.top - layoutManager.getTopDecorationHeight(targetView))
                            }else{
                                scrollBy(0, targetView.top - layoutManager.getTopDecorationHeight(targetView))
                            }
                            removeOnScrollListener(this)
                        }else  if (orientation == RecyclerView.HORIZONTAL && targetView != null){
                            if(smooth){
                                smoothScrollBy(targetView.left - layoutManager.getLeftDecorationWidth(targetView), 0)
                            }else{
                                scrollBy(targetView.left - layoutManager.getLeftDecorationWidth(targetView), 0)
                            }
                            removeOnScrollListener(this)
                        }
                    }
                }
            }
        })
    } else {
        when (val layoutManager = layoutManager) {
            is LinearLayoutManager,is GridLayoutManager,is StaggeredGridLayoutManager -> {
                if (orientation == RecyclerView.VERTICAL) {
                    if(smooth){
                        smoothScrollBy(0, findView.top - layoutManager.getTopDecorationHeight(findView))
                    }else{
                        scrollBy(0, findView.top - layoutManager.getTopDecorationHeight(findView))
                    }
                }else  if (orientation == RecyclerView.HORIZONTAL){
                    if(smooth){
                        smoothScrollBy(findView.left - layoutManager.getLeftDecorationWidth(findView), 0)
                    }else{
                        scrollBy(findView.left - layoutManager.getLeftDecorationWidth(findView), 0)
                    }
                }
            }
        }
    }
}