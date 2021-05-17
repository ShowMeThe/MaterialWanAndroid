package com.show.kcore.glide

import android.graphics.drawable.Drawable

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.NoTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory


class DrawableScaleFadeFactory(
    private val duration: Int,
    private val isCrossFadeEnabled: Boolean,
    private val isReveal: Boolean = false,
    private val isScale: Boolean = false
) : TransitionFactory<Drawable> {
    private var resourceTransition: DrawableScaleFadeTransition? = null

    override fun build(dataSource: DataSource, isFirstResource: Boolean): Transition<Drawable> {
        return if (dataSource == DataSource.MEMORY_CACHE)
            NoTransition.get()
        else
            getResourceTransition()
    }


    private fun getResourceTransition(): Transition<Drawable> {
        if (resourceTransition == null) {
            resourceTransition =
                DrawableScaleFadeTransition(duration, isCrossFadeEnabled, isReveal, isScale)
        }
        return resourceTransition!!
    }


    class Builder
    /** @param durationMillis The duration of the cross fade animation in milliseconds.
     */
    @JvmOverloads constructor(private val durationMillis: Int = DEFAULT_DURATION_MS) {
        private var isCrossFadeEnabled: Boolean = false


        fun setCrossFadeEnabled(isCrossFadeEnabled: Boolean): Builder {
            this.isCrossFadeEnabled = isCrossFadeEnabled
            return this
        }

        fun build(): DrawableScaleFadeFactory {
            return DrawableScaleFadeFactory(durationMillis, isCrossFadeEnabled)
        }

        companion object {
            private const val DEFAULT_DURATION_MS = 300
        }
    }
}
