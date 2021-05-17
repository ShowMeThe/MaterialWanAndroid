package com.show.kcore.dialog

import android.view.Gravity

@Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class  WindowParam(val gravity:Int = Gravity.CENTER,
                              val styleName :String = "BaseDialogStyle",
                              val outSideCanceled:Boolean = true,val animRes :Int = -1,
                              val canceled:Boolean = true, val dimAmount :Float = -1f)

data class WindowParams(val gravity:Int = Gravity.CENTER,
                        val styleName :String = "BaseDialogStyle",
                        val outSideCanceled:Boolean = true,val animRes :Int = -1,
                        val canceled:Boolean = true, val dimAmount :Float = -1f)

data class BottomWindowParams(val gravity:Int = Gravity.CENTER,
                        val styleName :String = "BaseBottomSheetStyle",
                        val outSideCanceled:Boolean = true,val animRes :Int = -1,
                        val canceled:Boolean = true, val dimAmount :Float = -1f)