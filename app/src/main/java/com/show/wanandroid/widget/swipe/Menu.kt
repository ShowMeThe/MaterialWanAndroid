package com.show.wanandroid.widget.swipe

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue


open class Menu {
    var backgroundColor = Color.RED
    var tintColor = Color.WHITE
    var menuWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        100f, Resources.getSystem().displayMetrics)
    var menuPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        15f, Resources.getSystem().displayMetrics)
}


class TextMenu (call: TextMenu.() -> Unit) : Menu() {


    var text = ""
    var textSize = 15f
    var textColor = tintColor
    var drawableStart: Drawable? = null
    var drawableEnd: Drawable? = null
    var drawableTop: Drawable? = null
    var drawableBottom: Drawable? = null
    var drawablePadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        15f, Resources.getSystem().displayMetrics)

    init {
        call.invoke(this)
    }

}

class ImageMenu(call: ImageMenu.() -> Unit) : Menu() {

    var drawable: Drawable? = null

    init {
        call.invoke(this)
    }
}