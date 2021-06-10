package com.showmethe.skinlib

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EdgeEffect

object ReflectEdge {

    private val names by lazy {
        arrayOf(
            "mLeftGlow",
            "mTopGlow",
            "mRightGlow",
            "mBottomGlow",
            "mEdgeGlowTop",
            "mEdgeGlowBottom"
        )
    }

    private val methodsName by lazy {
        arrayOf(
            "ensureLeftGlow",
            "ensureRightGlow",
            "ensureTopGlow",
            "ensureBottomGlow",
            "ensureGlows"
        )
    }

    fun reflectColor(viewGroup: View, color: Int) {
        kotlin.runCatching {
            names.forEach {
                reflectEach(viewGroup, color, it)
            }
        }
    }

    private fun reflectEach(viewGroup: View, color: Int, name: String) {
        kotlin.runCatching {
            val clazz = viewGroup::class.java
            val catchView = tryToFindRv(viewGroup)
            if (catchView != null) {
                val clazz2 = catchView::class.java.superclass
                val field = clazz2?.getDeclaredField(name)
                field?.isAccessible = true
                var edgeEffect = field?.get(catchView) as EdgeEffect?
                if (edgeEffect == null) {
                    invokeMethod(catchView,clazz2)
                    edgeEffect = field?.get(catchView) as EdgeEffect?
                    edgeEffect?.color = color
                } else {
                    edgeEffect.color = color
                }
                field?.set(catchView, edgeEffect)

            }else{
                invokeMethod(viewGroup,clazz)
                val field = clazz.getDeclaredField(name)
                field.isAccessible = true
                val edgeEffect = field.get(viewGroup) as EdgeEffect?
                edgeEffect?.color = color
                field.set(viewGroup, edgeEffect)
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun tryToFindRv(viewGroup: View): View? {
        return kotlin.runCatching {
            val clazz = viewGroup::class.java
            val field = clazz.getDeclaredField("mRecyclerView")
            field.isAccessible = true
            field.get(viewGroup) as View?
        }.getOrNull()
    }

    private fun invokeMethod(catchView: View, clazz2: Class<*>?) {
        kotlin.runCatching {
            methodsName.forEach {
                val method = clazz2?.getDeclaredMethod(it)
                method?.isAccessible = true
                method?.invoke(catchView)
            }
        }
    }
}