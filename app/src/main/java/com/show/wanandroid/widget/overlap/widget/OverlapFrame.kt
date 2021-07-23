package com.show.wanandroid.widget.overlap.widget

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.show.kcore.extras.display.dp
import com.show.wanandroid.widget.overlap.Level
import com.show.wanandroid.widget.overlap.LevelChildren
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class OverlapFrame @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mDefaultBgColor = Color.parseColor("#696969")

    private val mSrcOutMode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

    private val defaultBubbleLayoutParams by lazy {
        LayoutParams(
            LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private val padding = 2f.dp.toInt()

    private val insidePadding = 0f.dp

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
            addViewInLayout(pair.first, -1, pair.second, false)
        }
    }

    private fun createBubbleWithXY(child: LevelChildren): Pair<View, LayoutParams> {
        val bubbleLayout = BubbleLayout(context)
        val textView = createText(child)
        bubbleLayout.addView(textView, FrameLayout
            .LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                gravity = child.labelTextGravity
            })
        textView.setPadding(padding, padding, padding, padding)
        val x = child.centerX
        val y = child.centerY
        val radius = child.radius
        val archId = View.generateViewId()
        val archView = View(context).also {
            it.id = archId
        }
        addViewInLayout(archView, -1, LayoutParams(1, 1).apply {
            topToTop = 0
            startToStart = 0
            topMargin = y.toInt()
            leftMargin = x.toInt()
        })

        val layoutParams = LayoutParams(child.labelWidth.toInt(), child.labelHeight.toInt())

        val tranRadius = child.circleAngle - (child.circleAngle / 90).toInt() * 90
        val corner = abs(90 - tranRadius)
        val sin = abs(sin(Math.toRadians(90.toDouble())))
        layoutParams.apply {
            circleConstraint = archId
            circleAngle = child.circleAngle
            circleRadius = (sin * child.labelWidth / 2f  + radius + insidePadding).toInt()
        }
        bubbleLayout.setBackGroundColor(child.labelColor)
        bubbleLayout.setEdge(child.edge)
        bubbleLayout.setOffset(child.offset)

        /*if (x < measuredWidth / 2f && y < measuredHeight / 2f) {

        } else if (x >= measuredWidth / 2f && y < measuredHeight / 2f) {
            layoutParams.apply {
                topToTop = 0
                endToEnd = 0
                topMargin = (y - child.labelHeight / 2f).toInt()
                rightMargin = ((measuredWidth - x) + radius + insidePadding).toInt()

            }
            bubbleLayout.setBackGroundColor(child.labelColor)
            bubbleLayout.setEdge(child.edge)
            bubbleLayout.setOffset(child.offset)

        } else if (x >= measuredWidth / 2f && y >= measuredHeight / 2f) {
            layoutParams.apply {
                bottomToBottom = 0
                endToEnd = 0
                bottomMargin = ((measuredHeight - y) - child.labelHeight / 2f).toInt()
                rightMargin = ((measuredWidth - x) + radius + insidePadding).toInt()

            }
            bubbleLayout.setBackGroundColor(child.labelColor)
            bubbleLayout.setEdge(child.edge)
            bubbleLayout.setOffset(child.offset)
        } else if (x < measuredWidth / 2f && y >= measuredHeight / 2f) {
            layoutParams.apply {
                bottomToBottom = 0
                startToStart = 0
                bottomMargin = ((measuredHeight - y) - child.labelHeight / 2f).toInt()
                leftMargin = (x + radius + insidePadding).toInt()

            }
            bubbleLayout.setBackGroundColor(child.labelColor)
            bubbleLayout.setEdge(child.edge)
            bubbleLayout.setOffset(child.offset)
        }*/

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
        mPaint.alpha = (255 * 0.8f).toInt()
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