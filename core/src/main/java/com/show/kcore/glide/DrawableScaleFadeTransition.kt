package com.show.kcore.glide

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import androidx.palette.graphics.Palette

import com.bumptech.glide.request.transition.Transition


class DrawableScaleFadeTransition(
    private val duration: Int,
    private val isCrossFadeEnabled: Boolean,
    private val isReveal: Boolean = false,
    private val isScale: Boolean = false
) : Transition<Drawable> {


    override fun transition(current: Drawable, adapter: Transition.ViewAdapter): Boolean {
        var previous = adapter.currentDrawable
        var bitmap:Bitmap? = null
        if (previous == null) {
            bitmap = createBitmap(current)
            previous = ColorDrawable(Palette.from(bitmap).generate().getVibrantColor(Color.TRANSPARENT))
        }
        bitmap?.also {
            if(!it.isRecycled){
                it.recycle()
            }
        }
        val view = adapter.view
        val transitionDrawable = TransitionDrawable(arrayOf(previous, current))
        transitionDrawable.isCrossFadeEnabled = isCrossFadeEnabled
        transitionDrawable.startTransition(duration * 2)
        adapter.setDrawable(transitionDrawable)
        if (view.visibility == View.VISIBLE && view.isAttachedToWindow) {
            if (isReveal) {
                val maxRadius = view.height.coerceAtLeast(view.width)
                val animation = ViewAnimationUtils.createCircularReveal(
                    view,
                    transitionDrawable.intrinsicWidth / 2,
                    transitionDrawable.intrinsicHeight / 2,
                    0f,
                    maxRadius.toFloat()
                )
                animation.duration = duration.toLong()
                animation.start()
            }
            if (isScale) {
                view.scaleX = 1.2f
                view.scaleY = 1.2f
                view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(duration.toLong()).start()
            }
        } else {
            view.scaleX = 1.0f
            view.scaleY = 1.0f
        }
        return true
    }

    private fun createBitmap(current: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        val drawable = current.constantState?.newDrawable()?: current
        drawable.setBounds(0, 0, 10, 10)
        drawable.draw(canvas)
        return bitmap
    }

}