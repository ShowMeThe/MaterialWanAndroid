package com.show.kcore.glide

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.show.kcore.extras.blur.BlurK


import java.security.MessageDigest


class BlurImageTransform constructor(var radius: Float = 15f) : BitmapTransformation() {


    override fun transform(pool: BitmapPool, source: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {

        var result: Bitmap? = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(result!!)
        val paint = Paint()
        canvas.drawBitmap(source, 0f, 0f, paint)
        result = BlurK.getBlur().process(result, radius)

        return result
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }


}
