package com.show.wanandroid.widget.overlap.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.show.kcore.extras.display.dp
import com.show.wanandroid.widget.overlap.Level
import com.show.wanandroid.widget.overlap.LevelChildren

class OverlapFrame @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mDefaultBgColor = Color.parseColor("#696969")

    private val mSrcOutMode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

    private val defaultTextLayoutParams by lazy { LayoutParams(LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT) }

    private val defaultBubbleLayoutParams by lazy { LayoutParams(LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT) }

    private val padding = 2f.dp.toInt()

    private val mPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = mDefaultBgColor
    }

    init {
        isClickable = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusable = FOCUSABLE
        }
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var level = Level()

    fun updateLevel(level: Level) {
        this.level = level
        post {
            updateLabel()
        }
        postInvalidate()
    }

    private fun updateLabel() {
        removeAllViews()
        level.children.forEachIndexed { index, it ->
            val pair = createBubbleWithXY(it)
            addViewInLayout(pair.first,index,pair.second,false)
        }
    }

    private fun createBubbleWithXY(child: LevelChildren): Pair<View, LayoutParams> {
        val bubbleLayout = BubbleLayout(context)
        val textView = createText(child)
        bubbleLayout.addView(textView, defaultTextLayoutParams)
        textView.setPadding(padding,padding,padding,padding)
        val x = child.centerX
        val y = child.centerY
        val radius = child.radius
        val layoutParams =  LayoutParams(child.labelWidth.toInt(),child.labelHeight.toInt())
        if(x < measuredWidth / 2f && y < measuredHeight /2f){
            layoutParams.apply {
                topToTop = 0
                startToStart = 0
                topMargin = (y).toInt()
                leftMargin = (x + radius).toInt()
            }
            bubbleLayout.setBackGroundColor(child.labelColor)
            bubbleLayout.setEdge(child.edge)
        }

        return bubbleLayout to layoutParams
    }

    private fun createText(child: LevelChildren): TextView {
        val textView = AppCompatTextView(context)
        textView.text = child.labelText
        textView.setTextColor(child.labelTextColor)
        return textView
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()

        mPaint.color = level.backgroundColor
        mPaint.alpha = (255 * 0.75f).toInt()
        mPaint.xfermode = null
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), mPaint)


        level.children.forEach {
            if (it.radius != 0f) {
                mPaint.xfermode = mSrcOutMode
                canvas.drawCircle(it.centerX, it.centerY, it.radius, mPaint)
            }
        }

        canvas.restore()
    }

}