package com.show.kcore.base

import android.app.ActivityOptions
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import com.show.kInject.core.initScope
import com.show.kInject.lifecyleowner.module.lifeModule
import com.show.kcore.extras.binding.Binding
import com.show.kcore.extras.binding.ViewDataBindRef
import com.show.kcore.extras.keyborad.hideSoftKeyboard
import com.show.kcore.extras.log.Logger
import java.lang.reflect.ParameterizedType


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
abstract class BaseActivity<V : ViewBinding, VM : AndroidViewModel> : AppCompatActivity() {


    private var onResult: ((resultCode: Int, data: Intent?) -> Unit)? = null
    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onResult?.invoke(it.resultCode, it.data)
        }
    private val receiver by lazy { LifeActiveReceiver() }
    var binding: V by ViewDataBindRef()

    val viewModel: VM by lazy {
        val types = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val clazz = types[1] as Class<VM>
        ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(clazz)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        Binding.getBinding(this)?.apply {
            binding = this
        }
        setUpTransition()

        AppManager.get().addActivityCls(this::class.java.name)

        initScope {
            module(viewModel, lifeModule {
                scopeLifeOwner(viewModel)
            })
        }

        intent.apply {
            extras?.apply {
                BundleAutoInject.inject(this@BaseActivity)
                onBundle(this)
            }
        }

        registerReceiver(receiver, IntentFilter().apply {
            addAction(this@BaseActivity::class.java.name)
        })



        observerUI()
        init(savedInstanceState)
        initListener()

    }


    inline fun <reified VM : ViewModel> createViewModel(): VM {
        return viewModels<VM> { ViewModelProvider.AndroidViewModelFactory(application) }.value
    }


    abstract fun getViewId(): Int

    //abstract fun initViewModel() : VM

    abstract fun onBundle(bundle: Bundle)

    abstract fun observerUI()

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initListener()


    override fun onBackPressed() {
        finishAfterTransition()
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideSoftKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    // 如果焦点不是EditText则忽略
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }


    /**
     * startActivity，加入切换动画
     */
    inline fun <reified T> startActivity(bundle: Bundle? = null, transition: Boolean = false) {
        val intent = Intent(this, T::class.java)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(
            intent, if (transition) {
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            } else {
                null
            }
        )
    }


    inline fun <reified T> startActivityWithPair(
        bundle: Bundle? = null,
        vararg pair: android.util.Pair<View, String>
    ) {
        val intent = Intent(this, T::class.java)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (pair.isNotEmpty()) {
            val transitionActivityOptions =
                ActivityOptions.makeSceneTransitionAnimation(this, *pair)
            startActivity(intent, transitionActivityOptions.toBundle())
        } else {
            startActivity(intent)
        }
    }


    fun startForResult(
        bundle: Bundle?,
        target: Class<*>,
        onResult: ((resultCode: Int, data: Intent?) -> Unit)) {
        val intent = Intent(this, target)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        this.onResult = onResult
        result.launch(intent)
    }


    override fun onDestroy() {
        AppManager.get().removeActivity(this::class.java.name)
        unregisterReceiver(receiver)
        super.onDestroy()

    }


    inner class LifeActiveReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.apply {
                if (this@BaseActivity::class.java.name == action) {
                    Logger.dLog("LifeActiveReceiver","finishAfterTransition")
                    onBackPressed()
                }
            }
        }
    }

    fun binding(block: V.() -> Unit) {
        block.invoke(binding)
    }


}