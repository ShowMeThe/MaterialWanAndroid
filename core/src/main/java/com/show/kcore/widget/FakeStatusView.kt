package com.show.kcore.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.show.kcore.extras.status.Immerse
import com.show.kcore.extras.status.getStatusBarHeight

/**
 *  com.show.kcore.widget
 *  2020/7/20
 *  22:18
 *  ShowMeThe
 */
class FakeStatusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = context.getStatusBarHeight()
        setMeasuredDimension(widthMeasureSpec,height)
    }
}