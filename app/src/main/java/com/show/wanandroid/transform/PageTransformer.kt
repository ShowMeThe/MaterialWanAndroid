package com.show.wanandroid.transform

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 *  com.show.wanandroid.transform
 *  2020/3/21
 *  10:41
 */
class PageTransformer : ViewPager.PageTransformer {
    private val scale = 0.75f
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> {
                page.alpha = 0f
            }
            position <= 0 -> {
                page.alpha = 1f
            }
            position <= 1 -> {
                page.alpha = 1 - position
            }
            else -> {
                page.alpha = 0f
            }
        }
    }
}