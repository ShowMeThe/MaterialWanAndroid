package com.show.wanandroid.widget.overlap

import android.view.ViewGroup
import android.view.Window
import com.show.wanandroid.widget.overlap.widget.OverlapFrame

class OverLap private constructor(private val builder: OverLapBuilder) {

    companion object {

        fun builder(window: Window): OverLapBuilder {
            return OverLapBuilder(window)
        }

    }

    private fun buildView(): OverLap {
        val window = builder.window
        val context = window.context
        val overLapView = OverlapFrame(context)

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )


        val levels = builder.level
        if (levels.isNotEmpty()) {
            overLapView.updateLevel(levels[0])
        }

        overLapView.setOnClickListener {
            if (levels.isNotEmpty()) {
                levels.remove(levels.first())
            } else {

                return@setOnClickListener
            }
            if (levels.isNotEmpty()) {
                overLapView.updateLevel(levels.first())
            } else {
                (overLapView.parent as ViewGroup?)?.removeView(overLapView)
            }
        }

        window.addContentView(overLapView, params)

        return this
    }


    class OverLapBuilder(val window: Window) {

        val level = ArrayList<Level>()

        fun levels(vararg level: Level): OverLapBuilder {
            this.level.addAll(level)
            return this

        }

        fun build(): OverLap {
            return OverLap(this)
                .buildView()
        }
    }

}