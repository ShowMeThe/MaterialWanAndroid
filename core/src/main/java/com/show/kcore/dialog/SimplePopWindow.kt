package com.show.kcore.dialog

import android.app.ActionBar
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.show.kcore.R
import com.show.kcore.extras.binding.Binding.binding


abstract class SimplePopWindow(private val context: Context){

    private var onView :((view: View)->Unit)? = null

    private var popupWidth = 0
    private var popupHeight = 0

    private lateinit var popWindow : PopupWindow


    abstract fun getViewId() :Int

    /**
     * @return PopupWindow width and height
     */
    abstract fun build(context:Context?) :Pair<Int,Int>

    fun doCreate(){
        popWindow = PopupWindow(context)
        val pair = build(context)
        val viewId = getViewId()
        viewId.apply {
            val view = View.inflate(context, viewId, null)
            with(popWindow){
                width = pair.first
                height = pair.second
                contentView = view
                isFocusable = true
                isOutsideTouchable = true
                animationStyle = R.style.AnimAlpha
                setBackgroundDrawable(ColorDrawable(0x00000000))
            }

            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            popupWidth = view.measuredWidth
            popupHeight = view.measuredHeight


            view.apply {
                onView?.invoke(this)
            }
        }
    }

    fun showAsDropDown(v: View){
        popWindow.showAsDropDown(v)
    }

    open fun showAtEnd(v : View){
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        popWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] + v.width, location[1])
    }


    open fun showAtStart(v : View){
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        popWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - popupWidth, location[1])
    }


    open  fun showAtTop(v : View){
        val location = IntArray(2)
        v.getLocationOnScreen(location)
        popWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.width / 2) - popupWidth / 2, location[1] - popupHeight)
    }


    open  fun showAtBottom(view: View) {
        popWindow.showAsDropDown(view)
    }



    inline fun <reified T : ViewBinding> View.onBindingView(onBindingView: (T.() -> Unit)) {
        onBindingView.invoke(binding<T>(this)!!)
    }


    fun onView(onView :((view:View)->Unit)) : SimplePopWindow {
        this.onView = onView
        return this
    }

    fun dismiss(){
        popWindow.dismiss()
    }

    fun getWindow() = popWindow

}