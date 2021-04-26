package com.show.kcore.base

import android.animation.Animator
import android.animation.TimeInterpolator
import android.graphics.Color
import android.transition.*
import android.util.Log
import android.view.*
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import androidx.core.animation.doOnEnd
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import com.show.kcore.R
import kotlin.math.sqrt


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Transition(val mode: TransitionMode = TransitionMode.Fade ,val wholeWidget:Boolean = true)

enum class TransitionMode(var value: Int) {

    SlideStart(Gravity.START), SlideEnd(Gravity.END),
    SlideTop(Gravity.TOP), SlideBottom(Gravity.BOTTOM),
    Explode(-1), RevealTopLeft(1), RevealTopRight(2),
    RevealCenter(3), RevealBottomLeft(4),
    RevealBottomRight(5), Fade(-3)
}


fun AppCompatActivity.setUpTransition() {
    //window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        val ano = this@setUpTransition::class.java.getAnnotation(Transition::class.java)
        if (ano != null) {
            val wholeWidget = ano.wholeWidget
            if(wholeWidget){
                val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
                if(rootView.background == null){
                    val ids = intArrayOf(android.R.attr.windowBackground)
                    val array = theme.obtainStyledAttributes(ids)
                    val color = array.getColor(0, Color.TRANSPARENT)
                    rootView.setBackgroundColor(color)
                    array.recycle()
                }
            }
            val fade = Fade()
            when (ano.mode) {
                TransitionMode.RevealTopLeft, TransitionMode.RevealTopRight,
                TransitionMode.RevealCenter, TransitionMode.RevealBottomLeft,
                TransitionMode.RevealBottomRight -> {
                    val revealTransition = RevealTransition(ano.mode.value)
                    val interpolator = LinearInterpolator()
                    val set = TransitionSet()
                        .setDuration(resources.getInteger(R.integer.window_reveal_transition_duration).toLong())
                        .addTransition(revealTransition)
                        .addTransition(fade)
                        .setInterpolator(interpolator)
                        .setOrdering(TransitionSet.ORDERING_TOGETHER)

                    window.exitTransition = set
                    window.enterTransition = set

                }
                else -> {
                    val transition: android.transition.Transition? = Slide().let {
                        when (ano.mode) {
                            TransitionMode.Explode -> {
                                Explode()
                            }
                            TransitionMode.Fade -> {
                                null
                            }
                            else -> {
                                it.slideEdge = ano.mode.value
                                it
                            }
                        }
                    }
                    val set = TransitionSet()
                        .setDuration(resources.getInteger(R.integer.window_transition_duration).toLong())
                        .apply {
                            if (transition != null) {
                                addTransition(transition)
                            }
                        }
                        .addTransition(fade)
                        .setInterpolator(LinearInterpolator())
                        .setOrdering(TransitionSet.ORDERING_TOGETHER)


                    window.exitTransition = set
                    window.enterTransition = set

                }
            }
        }
}


class RevealTransition(val type:Int) : Visibility() {

    private val height = "RevealTransition:height"
    private val width = "RevealTransition:width"
    private val root = "RevealTransition:rootView"

    private var startAnimator : Animator? = null
    private var endAnimator : Animator? = null

    override fun captureStartValues(value: TransitionValues) {
        val rootView = (value.view.rootView as ViewGroup).getChildAt(0)
        value.values[height] = rootView.height
        value.values[width] = rootView.width
        value.values[root] = rootView
    }

    override fun captureEndValues(value: TransitionValues) {
        val rootView = (value.view.rootView as ViewGroup).getChildAt(0)
        value.values[height] = rootView.height
        value.values[width] = rootView.width
        value.values[root] = rootView
    }

    override fun onDisappear(
        sceneRoot: ViewGroup,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator {
        return  getAnimator(endValues,false)
    }

    override fun onAppear(
        sceneRoot: ViewGroup?,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator {
        return getAnimator(endValues,true)
    }


    private fun getAnimator(value: TransitionValues,startToEnd:Boolean) : Animator{
        val startView = value.values[root] as View
        val height = value.values[height] as Int
        val width = value.values[width] as Int
        val radius = sqrt((height * height + width * width).toDouble() * if(type == 3) 0.25f else 1f).toFloat()
        var centerX = 0
        var centerY = 0
        when(type){
            1 -> {
                centerX = 0
                centerY = 0
            }
            2 -> {
                centerX = width
                centerY = 0
            }
            3 -> {
                centerX = (width/2f).toInt()
                centerY = (height/2f).toInt()
            }
            4 ->{
                centerX = 0
                centerY = height
            }
            5 ->{
                centerX = width
                centerY = height
            }
        }

        val animator = ViewAnimationUtils.createCircularReveal(startView, centerX, centerY,
            if(startToEnd) 0f else radius, if(!startToEnd) 0f else radius)
        val noPauseAnimator = NoPauseAnimator(animator)
        noPauseAnimator.duration = duration
        noPauseAnimator.interpolator = interpolator
        return noPauseAnimator
    }


}


private class NoPauseAnimator(private val mAnimator: Animator) : Animator() {
    private val mListeners: ArrayMap<AnimatorListener, AnimatorListener> =
        ArrayMap<AnimatorListener, AnimatorListener>()

    override fun addListener(listener: AnimatorListener) {
        val wrapper: AnimatorListener = AnimatorListenerWrapper(
            this,
            listener
        )
        if (!mListeners.containsKey(listener)) {
            mListeners[listener] = wrapper
            mAnimator.addListener(wrapper)
        }
    }

    override fun cancel() {
        mAnimator.cancel()
    }

    override fun end() {
        mAnimator.end()
    }

    override fun getDuration(): Long {
        return mAnimator.duration
    }

    override fun getInterpolator(): TimeInterpolator {
        return mAnimator.interpolator
    }

    override fun getListeners(): ArrayList<AnimatorListener> {
        return ArrayList<AnimatorListener>(mListeners.keys)
    }

    override fun getStartDelay(): Long {
        return mAnimator.startDelay
    }

    override fun isPaused(): Boolean {
        return mAnimator.isPaused
    }

    override fun isRunning(): Boolean {
        return mAnimator.isRunning
    }

    override fun isStarted(): Boolean {
        return mAnimator.isStarted
    }

    override fun removeAllListeners() {
        super.removeAllListeners()
        mListeners.clear()
        mAnimator.removeAllListeners()
    }

    override fun removeListener(listener: AnimatorListener) {
        val wrapper: AnimatorListener? = mListeners.get(listener)
        if (wrapper != null) {
            mListeners.remove(listener)
            mAnimator.removeListener(wrapper)
        }
    }

    /* We don't want to override pause or resume methods
     * because we don't want them to affect mAnimator.
    public void pause();
    public void resume();
    public void addPauseListener(AnimatorPauseListener listener);
    public void removePauseListener(AnimatorPauseListener listener);
     */
    override fun setDuration(durationMS: Long): Animator {
        mAnimator.duration = durationMS
        return this
    }

    override fun setInterpolator(timeInterpolator: TimeInterpolator) {
        mAnimator.interpolator = timeInterpolator
    }

    override fun setStartDelay(delayMS: Long) {
        mAnimator.startDelay = delayMS
    }

    override fun setTarget(target: Any?) {
        mAnimator.setTarget(target)
    }

    override fun setupEndValues() {
        mAnimator.setupEndValues()
    }

    override fun setupStartValues() {
        mAnimator.setupStartValues()
    }

    override fun start() {
        mAnimator.start()
    }
}

private class AnimatorListenerWrapper(
    private val mAnimator: Animator,
    private val mListener: Animator.AnimatorListener
) : Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
        mListener.onAnimationStart(animation, isReverse)
    }

    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
        mListener.onAnimationEnd(mAnimator,isReverse)
    }

    override fun onAnimationStart(animator: Animator) {
        mListener.onAnimationStart(mAnimator)
    }

    override fun onAnimationEnd(animator: Animator) {
        mListener.onAnimationEnd(mAnimator)
    }

    override fun onAnimationCancel(animator: Animator) {
        mListener.onAnimationCancel(mAnimator)
    }

    override fun onAnimationRepeat(animator: Animator) {
        mListener.onAnimationRepeat(mAnimator)
    }
}

