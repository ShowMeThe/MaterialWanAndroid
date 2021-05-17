package com.show.kcore.extras.blur

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.FloatRange

class BlurK {


    companion object {

        private val instant by  lazy { BlurK() }

        private lateinit var rs: RenderScript

        fun init(context: Context) {
            rs = RenderScript.create(context.applicationContext)
        }

        fun getBlur() : BlurK {
            return instant
        }
    }

    fun process(src: Bitmap, @FloatRange(from = 0.0,to = 25.0)  radius: Float): Bitmap {
        val input = Allocation.createFromBitmap(
            rs, src,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_SCRIPT)
        val output: Allocation = Allocation.createTyped(rs, input.type)
        val script  = ScriptIntrinsicBlur.create(
            rs, Element.U8_4(
                rs
            ))
        script.setRadius(radius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(src)
        return src
    }


}