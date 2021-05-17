package com.show.wanandroid.dialog

import android.os.Bundle

import com.show.kcore.dialog.SimpleDialogFragment
import com.show.kcore.dialog.WindowParam
import com.show.wanandroid.R
import com.show.wanandroid.databinding.DialogExitBinding
import com.showmethe.skinlib.SkinManager

@WindowParam(outSideCanceled = true)
class ExitDialog : SimpleDialogFragment() {
    override fun build(savedInstanceState: Bundle?) {
        buildDialog { R.layout.dialog_exit }
        onView {
            onBindingView<DialogExitBinding> {
                SkinManager.getManager().autoTheme(SkinManager.currentStyle,this)
                tvCancel.setOnClickListener {
                    dismiss()
                }

                tvConfirm.setOnClickListener {
                    onClick.invoke()
                    dismiss()
                }
            }
        }
    }

    private var onClick  = {}
    fun setOnConfirmClickListener(onClick:()->Unit){
        this.onClick = onClick
    }
}