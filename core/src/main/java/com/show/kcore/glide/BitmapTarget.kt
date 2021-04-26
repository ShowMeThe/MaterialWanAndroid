package com.show.kcore.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.util.Util

/**
 * Update Time: 2020/4/25
 * Package Name:showmethe.github.core.glide
 */
abstract class BitmapTarget constructor(val width: Int  = Target.SIZE_ORIGINAL
                                         ,val height: Int = Target.SIZE_ORIGINAL) : Target<Bitmap> {

    override fun onLoadStarted(placeholder: Drawable?) {

    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
    }

    override fun getSize(cb: SizeReadyCallback) {
        if (!Util.isValidDimensions(width, height)) {
            throw IllegalArgumentException(
                    "Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given" + " width: "
                            + width + " and height: " + height + ", either provide dimensions in the constructor"
                            + " or call override()")
        }
        cb.onSizeReady(width, height)
    }

    override fun getRequest(): Request? = null

    override fun onStop() {

    }

    override fun setRequest(request: Request?) {
    }

    override fun removeCallback(cb: SizeReadyCallback) {

    }

    override fun onLoadCleared(placeholder: Drawable?) {

    }

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
       if(!resource.isRecycled){
           resourceReady(resource,transition)
       }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

    abstract fun resourceReady(resource: Bitmap,transition: Transition<in Bitmap>?)
}