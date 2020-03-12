package com.show.wanandroid.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.show.wanandroid.R

/**
 * 摇晃ImageView
 */
class ShakingImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr){

    private var animator : ObjectAnimator? = null
    private var radius = 20f
    private var orientation = 0
    private var shakeAuto = false
    private var duration = 500
    init {

        initAttr(context,attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.ShakingImageView)
        orientation = array.getInt(R.styleable.ShakingImageView_shake_orientation,0)
        radius = array.getDimension(R.styleable.ShakingImageView_shake_radius,20f)
        shakeAuto  =array.getBoolean(R.styleable.ShakingImageView_shake_auto,false)
        duration = array.getInt(R.styleable.ShakingImageView_shake_duration,500)
        array.recycle()
        buildAnim()
    }

    private fun buildAnim(){
        val translation = if(orientation == 0){
            "translationX"
        } else{
            "translationY"
        }
        animator = ObjectAnimator.ofFloat(this,translation,-radius,radius)
        animator?.duration = duration.toLong()
        animator?.interpolator = AnticipateInterpolator()
        animator?.repeatCount = -1
        animator?.repeatMode = REVERSE
        if(shakeAuto){
            animator?.start()
        }
    }

    fun startAnimator(){
        animator?.start()
    }

    fun stopAnimator(){
        animator?.cancel()
        animator = null
    }

}