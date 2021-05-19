package com.show.wanandroid.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.textview.MaterialTextView


class MarqueeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr) {


    init {
        isFocusable = true
        isFocusableInTouchMode = true
        ellipsize = TextUtils.TruncateAt.MARQUEE
        isSingleLine = true
        marqueeRepeatLimit = -1
    }


    override fun isFocused(): Boolean {
        return true
    }
}