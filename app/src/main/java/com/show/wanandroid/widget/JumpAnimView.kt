package com.show.wanandroid.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import com.show.wanandroid.R

class JumpAnimView @JvmOverloads constructor(
    context: Context,  attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val text = "Android"
    private val interpolator = BounceInterpolator()
    private val textViews = ArrayList<TextView>()
    private var textColor = Color.parseColor("#333333")

    init {
        orientation = HORIZONTAL
        initAttr(attrs)
        init()
    }

    private fun initAttr(attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.JumpAnimView)
        textColor = array.getColor(R.styleable.JumpAnimView_text_color,Color.parseColor("#333333"))
        array.recycle()
    }


    private fun init() {
        text.toCharArray().forEach {
            val textView = TextView(context)
            textView.text = it.toString()
            textView.textSize = 32f
            textView.translationY = -50f
            textView.alpha = 0f
            textView.rotation = -90f
            textView.setTextColor(textColor)
            addView(textView,getDefaultLayoutParam())
            textViews.add(textView)
        }

        for((index,it) in textViews.withIndex()){
            it.animate().alpha(1.0f)
                .rotationBy(90f)
                .translationYBy(50f)
                .setDuration(550)
                .setInterpolator(interpolator)
                .setStartDelay(index * 50L).start()
        }

    }


    private fun getDefaultLayoutParam() = LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)

}