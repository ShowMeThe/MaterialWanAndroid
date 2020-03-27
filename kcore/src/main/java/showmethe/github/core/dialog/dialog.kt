package showmethe.github.core.dialog

import android.view.Gravity

@Target(AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class  WindowParam(val gravity:Int = Gravity.CENTER,
                              val outSideCanceled:Boolean = true, val noAnim : Boolean = true,val animRes :Int = -1,
                              val canceled:Boolean = true, val dimAmount :Float = -1f)