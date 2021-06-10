package com.show.wanandroid.dialog

import android.os.Bundle
import com.show.kcore.dialog.SimpleDialogFragment
import com.show.kcore.dialog.WindowParam
import com.show.wanandroid.R
import com.show.wanandroid.databinding.DialogLoadingBinding
import com.showmethe.skinlib.SkinManager

@WindowParam
class LoadingDialog : SimpleDialogFragment() {
    override fun build(savedInstanceState: Bundle?) {
        buildDialog { R.layout.dialog_loading }.onView {
            onBindingView<DialogLoadingBinding> {
                SkinManager.getManager().autoTheme(SkinManager.currentStyle, this)
            }
        }
    }
}