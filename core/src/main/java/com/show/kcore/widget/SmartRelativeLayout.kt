package com.show.kcore.widget

import android.content.Context
import android.content.res.ColorStateList
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.show.kcore.R

import java.lang.ref.WeakReference
import java.util.ArrayList


class SmartRelativeLayout @JvmOverloads constructor(context: Context,
                                                    val attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    private lateinit var loadingView: View
    private lateinit var emptyView: View
    private lateinit var errorView: View
    private var loadinglayout = loadingId
    private var emptyLayout = emptyId
    private var errorLayout = errorId
    private var currentState = 0
    private val views = ArrayList<View>()


    private val mHandler = Handler(Looper.getMainLooper(),
        WeakReference(Handler.Callback {
        when (it.what) {
            0 -> setViewState(LOADING_STATE)
            1 -> setViewState(EMPTY_STATE)
            2 -> setViewState(ERROR_STATE)
            4 -> setViewState(CONTENT_STATE)
        }
        true
    }).get())


    init {
        init(context)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs,R.styleable.SmartRelativeLayout)
        loadinglayout = array.getResourceId(R.styleable.SmartRelativeLayout_loading_view, loadingId)
        errorLayout = array.getResourceId(R.styleable.SmartRelativeLayout_error_view, errorId)
        emptyLayout = array.getResourceId(R.styleable.SmartRelativeLayout_empty_view, emptyId)
        array.recycle()
    }

    private fun init(context: Context) {
        initAttrs(context,attrs)

        loadingView = LayoutInflater.from(context).inflate(loadinglayout, null)
        loadingView.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(loadingView)

        emptyView = LayoutInflater.from(context).inflate(emptyLayout, null)
        emptyView.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(emptyView)

        errorView = LayoutInflater.from(context).inflate(errorLayout, null)
        errorView.layoutParams = DEFAULT_LAYOUT_PARAMS
        views.add(errorView)

        loadingView.isClickable = true
        errorView.isClickable = true
        emptyView.isClickable = true

        moveLayoutToTop(20f)

        val firstIndex = childCount - 1
        addViewInLayout(errorView, firstIndex + 1, DEFAULT_LAYOUT_PARAMS, true)
        addViewInLayout(emptyView, firstIndex + 2, DEFAULT_LAYOUT_PARAMS, true)
        addViewInLayout(loadingView, firstIndex + 3, DEFAULT_LAYOUT_PARAMS, true)

        setViewState(CONTENT_STATE)
    }



    fun attachToView(
        attachLoading:(View.()->Unit)? = null,
        attachError: (View.()->Unit)? = null,
        attachEmpty : (View.()->Unit)? = null){

        attachLoading?.invoke(loadingView)
        attachError?.invoke(errorView)
        attachEmpty?.invoke(emptyView)

    }


    fun showLoading() {
        mHandler.sendEmptyMessage(0)
    }

    fun showEmpty() {
        mHandler.sendEmptyMessage(1)
    }

    fun showError() {
        mHandler.sendEmptyMessage(2)
    }

    fun showContent() {
        mHandler.sendEmptyMessage(4)
    }


    /**
     * 当需要遮盖Button需要设置这个
     */
    fun moveLayoutToTop(dp:Float) {
        loadingView.elevation = dp
        emptyView.elevation = dp
        errorView.elevation = dp
    }


    private fun setViewState(state: Int) {
        this.currentState = state
        for (i in views.indices) {
            if (state == i) {
                views[i].visibility = View.VISIBLE
            } else {
                views[i].visibility = View.GONE
            }
        }

    }



    companion object {

        private val loadingId = R.layout.smart_loading_layout
        private val emptyId = R.layout.smart_empty_layout
        private val errorId = R.layout.smart_error_layout
        const val LOADING_STATE = 0
        const val EMPTY_STATE = 1
        const val ERROR_STATE = 2
        const val CONTENT_STATE = 4


        private val DEFAULT_LAYOUT_PARAMS = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }


}
