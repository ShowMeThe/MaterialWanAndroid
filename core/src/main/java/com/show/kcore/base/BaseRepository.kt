package com.show.kcore.base

import androidx.lifecycle.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.show.kInject.lifecyleowner.ext.getLifeOwner
import com.show.kcore.extras.log.Logger

import java.lang.ref.WeakReference
import java.security.acl.Owner

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
open class BaseRepository(var viewModel: ViewModel?) : LifecycleObserver {


    private var owner : LifecycleOwner? = null
    init {
        init()
        Logger.eLog(BaseRepository::class.java.name,"Inject $viewModel")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        onClear()
    }

    private fun init() {
        if(owner == null){
            owner = getLifeOwner(viewModel)
        }
        owner?.lifecycle?.addObserver(this)
    }


    /**
     * 适当使用避免造成内存泄漏
     */
    private fun onClear() {
        owner?.lifecycle?.removeObserver(this)
        owner = null
    }


    fun androidScope(scope:LifecycleOwner?.()->Unit){
        if(owner == null){
            owner = getLifeOwner(viewModel)
            owner?.lifecycle?.addObserver(this)
        }
        scope.invoke(owner)
    }


}
