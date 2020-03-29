package com.showmethe.skinlib

import android.graphics.drawable.Drawable
import android.widget.EditText
/*
setTextCursorDrawable
*/

fun EditText.setEditTextCursor(drawableRes: Drawable?){
    try {
        val f = android.widget.TextView::class.java.getMethod("setTextCursorDrawable",Drawable::class.java)
        f.isAccessible = true
        f.invoke(this, drawableRes)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}