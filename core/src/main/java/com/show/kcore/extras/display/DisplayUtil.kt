package com.show.kcore.extras.display

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.show.kcore.base.AppContext
import kotlin.math.roundToInt




inline val Float.dp
   get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this,
       AppContext.get().context.resources.displayMetrics)

inline val Float.px
  get() = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this,
      AppContext.get().context.resources.displayMetrics) + 0.5).toInt()

inline val Float.sp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this,
        AppContext.get().context.resources.displayMetrics)


val screenW by lazy { AppContext.get().context.resources.displayMetrics.widthPixels }
val screenH by lazy { AppContext.get().context.resources.displayMetrics.widthPixels }