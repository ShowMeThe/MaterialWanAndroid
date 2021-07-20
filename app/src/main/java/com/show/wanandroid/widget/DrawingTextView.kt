package com.show.wanandroid.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.show.kcore.extras.counter.counter
import com.show.kcore.extras.display.dp
import com.show.wanandroid.R

class DrawingTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mInterpolator by lazy { FastOutLinearInInterpolator() }
    private val mDuration = 800L
    private var mAnimator: ValueAnimator? = null
    private var mPaintingText = ""
    private val mPaint by lazy {
        TextPaint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = 1f.dp
            typeface = Typeface.createFromAsset(context.assets,"Camicakan-2.otf")
        }
    }


    private var mTextSize = 24f.dp
    private lateinit var mTextMeasure: PathMeasure
    private var mProgress = 0.0f
    private val mDestRectPath by lazy { Path() }
    private var mFontRectPath = Path()


    init {
        initAttrs(attrs)
        initPath()
    }


    private fun initAttrs(attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.DrawingTextView)
        mPaintingText = array.getString(R.styleable.DrawingTextView_contentText) ?: ""
        mTextSize = array.getDimension(R.styleable.DrawingTextView_contentTextSize,mTextSize)


        mPaint.textSize = mTextSize
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = mPaint.measureText(mPaintingText).toInt()
        }
        if(heightMode == MeasureSpec.AT_MOST){
            val fontMetrics = mPaint.fontMetrics
            heightSize = (fontMetrics.descent - fontMetrics.ascent).toInt()
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthSize,widthMode),MeasureSpec.makeMeasureSpec(heightSize,heightMode))
    }


    private fun initPath() {
        mPaint.getTextPath(mPaintingText, 0, mPaintingText.length, 0f, mPaint.textSize, mFontRectPath)
        mTextMeasure = PathMeasure(mFontRectPath, false)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mTextMeasure.setPath(mFontRectPath, false)

        val length = mTextMeasure.length * mProgress
        mTextMeasure.getSegment(0f, length, mDestRectPath, true)
        canvas.drawPath(mDestRectPath, mPaint)

        while (mTextMeasure.nextContour()) {
            mTextMeasure.getSegment(0f, length, mDestRectPath, true)
            canvas.drawPath(mDestRectPath, mPaint)
        }
    }

    fun setText(text: String){
        mPaintingText = text
        requestLayout()
        initPath()
    }

    fun setTextColor(color: Int){
        mPaint.color = color
    }

    fun startDrawing() {
        if (mAnimator != null) {
            mAnimator?.cancel()
            mAnimator?.removeAllListeners()
            mAnimator = null
        }
        mAnimator = ValueAnimator.ofFloat(0f, 1f)
        mAnimator?.apply {
            interpolator = mInterpolator
            duration = mDuration
            addUpdateListener {
                mProgress = it.animatedValue as Float
                postInvalidate()
            }
            start()
        }
    }


}