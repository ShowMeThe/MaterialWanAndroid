package com.show.wanandroid.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.show.wanandroid.R
import java.util.*


class IconSwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object{
        const val STATE_DEFAULT = 1
        const val STATE_TRANSITION = 2
    }
    @FloatRange(from = 0.0,to = 1.0)
    var transitionPosition = 0f
    set(value) {
        field = value
        setPosition(field)
    }
    var state :Int = STATE_DEFAULT
    private var lock = false
    private var clickToTransition = true
    private var iconColor :ColorStateList? = null
    private var transitionColor :ColorStateList? = null
    private val imageViews = arrayOf(ImageView(context),ImageView(context))
    private var drawableDefault :Drawable? = null
    private var drawableTransition : Drawable? = null
    private val interceptor = FastOutLinearInInterpolator()
    private var duration = 500L

    init {
        initAttr(context,attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.IconSwitch)
        drawableDefault = array.getDrawable(R.styleable.IconSwitch_iconDefault)
        drawableTransition = array.getDrawable(R.styleable.IconSwitch_iconTransition)
        duration = array.getInteger(R.styleable.IconSwitch_transitionDuration,500).toLong()
        clickToTransition = array.getBoolean(R.styleable.IconSwitch_clickToTransition,true)
        iconColor = array.getColorStateList(R.styleable.IconSwitch_defaultTint)
        transitionColor = array.getColorStateList(R.styleable.IconSwitch_transitionTint)
        array.recycle()
        addView()
    }



    private fun addView(){
        if(drawableDefault == null || drawableTransition == null) return

        val default = imageViews[0]
        default.setImageDrawable(drawableDefault)


        val transition = imageViews[1]
        transition.setImageDrawable(drawableTransition)
        transition.scaleX = 0f
        transition.scaleY = 0f
        transition.alpha = 0f

        if(iconColor != null){
            default.imageTintList = iconColor
        }
        if(transitionColor!=null){
            transition.imageTintList = transitionColor
        }

        addView(default,getDefaultLayoutParam())
        addView(transition,getDefaultLayoutParam())

        setOnClickListener {
            if(clickToTransition){
                if(lock) return@setOnClickListener
                if(state == STATE_DEFAULT){
                    animTransition()
                }else{
                    animDefault()
                }
            }
            onSwitchClick?.invoke(state)
        }
    }

    fun animDefault(){
        toAnim(1f,0f)
    }

    fun animTransition(){
        toAnim(0f,1f)
    }

    private fun toAnim(startTarget:Float,endTarget:Float){
        imageViews[0].animate()
            .setDuration(duration)
            .alpha(startTarget)
            .scaleX(startTarget)
            .scaleY(startTarget)
            .setInterpolator(interceptor)
            .setListener(listener)
            .start()

        imageViews[1].animate()
            .setDuration(duration)
            .alpha(endTarget)
            .scaleX(endTarget)
            .scaleY(endTarget)
            .setInterpolator(interceptor)
            .start()
    }

    private val listener = object  : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            lock = true
        }
        override fun onAnimationEnd(animation: Animator?) {
            state = if(state == STATE_TRANSITION){
                STATE_DEFAULT
            }else{
                STATE_TRANSITION
            }
            lock = false
            onStateUpdate?.invoke(state)
        }
    }

    private fun setPosition(@FloatRange(from = 0.0,to = 1.0) position : Float){
        if(lock) return
        val default = if(state == STATE_DEFAULT){
            1 - position
        }else{
            position
        }

        val transition = if(state == STATE_DEFAULT){
                position
            }else{
            1- position
        }

        val defaultView = imageViews[0]
        defaultView.alpha = default.toFloat()
        defaultView.scaleX = default.toFloat()
        defaultView.scaleY = default.toFloat()

        val transitionView = imageViews[1]
        transitionView.alpha = transition.toFloat()
        transitionView.scaleX = transition.toFloat()
        transitionView.scaleY = transition.toFloat()
    }



    private var onSwitchClick : ((state:Int)->Unit)? = null
    fun setOnSwitchClickListener(onSwitchClick : ((state:Int)->Unit)){
        this.onSwitchClick = onSwitchClick
    }

    private var onStateUpdate : ((state:Int)->Unit)? = null
    fun setOnStateUpdateListener(onStateUpdate : ((state:Int)->Unit)){
        this.onStateUpdate = onStateUpdate
    }


    private fun getDefaultLayoutParam() = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )


}