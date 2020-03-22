package com.show.wanandroid.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.icu.text.MeasureFormat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.show.wanandroid.R
import com.showmethe.banner.Banner.Companion.REPEAT
import kotlin.math.abs
import kotlin.math.max

/**
 *  com.show.wanandroid.widget
 *  2020/3/22
 *  0:07
 */
class WaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var animator : ValueAnimator? =null
    private var divide = 10
    private val path = Path()
    private var wave = 30f
    private val paint = Paint()
    private val color = Color.parseColor("#0091EA")
    private var offset = 0f

    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        initAttr(context,attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.WaveView)
        paint.color = array.getColor(R.styleable.WaveView_wakeColor,Color.parseColor("#0091EA"))
        paint.strokeWidth = array.getDimension(R.styleable.WaveView_wakeWidth,10f)
        divide = array.getInt(R.styleable.WaveView_wakeCount,10)
        wave = array.getDimension(R.styleable.WaveView_wakeHeight,30f)
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        if(mode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthMeasureSpec, (wave * 2).toInt())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val height = measuredHeight.toFloat()
        val width = measuredWidth.toFloat()

        path.reset()
        calculatePath(width, height,0f)
        calculatePath(width, height, width / divide / 2f)
        calculatePath(width, height, width / divide)

        canvas.drawPath(path, paint)
    }


    private fun calculatePath(width: Float,height:Float,offsetX:Float){
        val divideOffset = width / divide
        val wakeOffset = divideOffset/2f
        path.moveTo(divideOffset * - divide * 2 + offset + offsetX,height/2f)
        for(i in -divide * 2 until divide){
            if(abs(i) % 2 == 0){
                path.cubicTo(divideOffset * i + offset + offsetX,height/2f,wakeOffset + divideOffset * i + offset + offsetX,height/2f - wave,
                    divideOffset + divideOffset*i + offset + offsetX,height/2f)
            }else{
                path.cubicTo(divideOffset * i + offset + offsetX,height/2f,wakeOffset + divideOffset * i + offset + offsetX,height/2f + wave,
                    divideOffset + divideOffset*i + offset + offsetX,height/2f)
            }
        }
    }

    fun startAnimator(){
        post {
            animator = ValueAnimator.ofFloat(0f,width.toFloat())
            animator?.duration = 2500
            animator?.repeatMode = REPEAT
            animator?.repeatCount = -1
            animator?.interpolator = LinearInterpolator()
            animator?.addUpdateListener {
                offset = it.animatedValue as Float
                postInvalidate()
            }
            animator?.start()
        }
    }

    fun stopAnimator(){
        animator?.cancel()
        animator = null
    }
}