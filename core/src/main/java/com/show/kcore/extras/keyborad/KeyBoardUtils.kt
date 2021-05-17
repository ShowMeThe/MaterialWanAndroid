package com.show.kcore.extras.keyborad

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText



fun EditText.closeKeyboard(): Boolean {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.hideSoftKeyboard() {
    val activity = this as Activity
    val view = activity.currentFocus
    if (view != null) {
        val inputMethodManager = getSystemService(
            Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun EditText.openKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

