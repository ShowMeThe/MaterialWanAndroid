package com.show.kcore.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.show.kInject.core.initScope
import com.show.kInject.lifecyleowner.module.lifeModule
import com.show.kcore.extras.binding.Binding
import com.show.kcore.extras.binding.ViewDataBindRef
import java.lang.reflect.ParameterizedType


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
abstract class BaseFragment<V : ViewBinding, VM : AndroidViewModel> : Fragment() {

    private var rootView: View? = null
    var binding: V by ViewDataBindRef()
    private var savedInstanceState: Bundle? = null

    val viewModel :VM by lazy {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[1] as Class<VM>
        ViewModelProvider(requireActivity().viewModelStore,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(clazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        if (arguments != null) {
            BundleAutoInject.inject(this)
            onBundle(requireArguments())
        }


        initScope {
            module(viewModel,lifeModule {
                scopeByName(viewModel.toString()) { requireActivity() } })
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = setContentView(inflater, container, getViewId())
        view?.apply {
            Binding.getBinding<V>(this@BaseFragment,view.rootView,0)?.apply {
                binding = this
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerUI()
        init(savedInstanceState)
        initListener()
    }



    inline fun <reified VM : ViewModel> createViewModel(bindActivity: Boolean = true): VM {
        return if (bindActivity) {
            activityViewModels<VM> { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }.value
        } else {
            return viewModels<VM> { ViewModelProvider.NewInstanceFactory() }.value
        }
    }


    /**
     * fragment可见的时候操作，取代onResume，且在可见状态切换到可见的时候调用
     */
    open fun onVisible() {


    }

    /**
     * fragment不可见的时候操作,onPause的时候,以及不可见的时候调用
     */
    open fun onHidden() {

    }


    override fun onResume() {//和activity的onResume绑定，Fragment初始化的时候必调用，但切换fragment的hide和visible的时候可能不会调用！
        super.onResume()
        if (isAdded && !isHidden) {//用isVisible此时为false，因为mView.getWindowToken为null
            onVisible()
        }
    }

    override fun onPause() {
        if (isVisible)
            onHidden()
        super.onPause()
    }



    //默认fragment创建的时候是可见的，但是不会调用该方法！切换可见状态的时候会调用，但是调用onResume，onPause的时候却不会调用
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onVisible()
        } else {
            onHidden()
        }
    }


    private fun setContentView(inflater: LayoutInflater, container: ViewGroup?, resId: Int): View? {
        if (rootView == null) {
            rootView = inflater.inflate(resId, container, false)
        } else {
            container?.removeView(rootView)
        }
        return rootView
    }


    //abstract fun initViewModel(): VM

    abstract fun getViewId(): Int

    abstract fun onBundle(bundle: Bundle)

    abstract fun observerUI()

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initListener()


    fun binding(block:V.()->Unit){
        block.invoke(binding)
    }

}