package com.show.wanandroid.widget.overlap.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.show.kcore.extras.display.dp


class BubbleLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {

        const val LEFT = 1
        const val TOP = 2
        const val RIGHT = 3
        const val BOTTOM = 4
    }

    private var direction = RIGHT

    private var radius = 10f.dp

    private var offset = 0.5f

    private val borderPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = mBackgroundColor
            setShadowLayer(shadowWidth.toFloat(), 0f, 0f, shadowColor)
        }
    }

    private val path = Path()

    private val rectF = RectF()

    private val shadowWidth = 2f.dp

    private val shadowColor = Color.parseColor("#999999")

    private var mBackgroundColor = Color.parseColor("#888888")

    private var distance = 10f.dp

    private var defaultPadding = distance

    private var mWidth: Int = 0
    private var mHeight: Int = 0


    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        distance = if (distance > defaultPadding) defaultPadding else distance


        when (direction) {
            LEFT -> drawLeftTriangle(canvas)
            TOP -> drawTopTriangle(canvas)
            RIGHT -> drawRightTriangle(canvas)
            BOTTOM -> drawBottomTriangle(canvas)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = measuredWidth
        mHeight = measuredHeight

        setPadding(
            defaultPadding.toInt(),
            defaultPadding.toInt(),
            defaultPadding.toInt(),
            defaultPadding.toInt()
        )
        rectF.left = defaultPadding
        rectF.top = defaultPadding
        rectF.right = w - defaultPadding
        rectF.bottom = h - defaultPadding
    }


    private fun drawLeftTriangle(canvas: Canvas) {
        val triangularLength = distance
        if (triangularLength == 0f) {
            return
        }

        path.addRoundRect(rectF, radius, radius, Path.Direction.CCW)

        val centerY = (rectF.bottom - rectF.top) * offset + defaultPadding
        val x = defaultPadding
        path.moveTo(x, centerY - triangularLength)
        path.lineTo(x - defaultPadding, centerY)
        path.lineTo(x, centerY + triangularLength)
        path.close()
        canvas.drawPath(path, borderPaint)
    }

    private fun drawTopTriangle(canvas: Canvas) {
        val triangularLength = distance
        if (triangularLength == 0f) {
            return
        }
        path.addRoundRect(rectF, radius, radius, Path.Direction.CCW)

        val centerX = (rectF.right - rectF.left) * offset + defaultPadding
        val y = defaultPadding

        path.moveTo(centerX - triangularLength, y)
        path.lineTo(centerX, y - triangularLength)
        path.lineTo(centerX + triangularLength, y)
        path.close()
        canvas.drawPath(path, borderPaint)
    }

    private fun drawRightTriangle(canvas: Canvas) {
        val triangularLength = distance
        if (triangularLength == 0f) {
            return
        }
        path.addRoundRect(rectF, radius, radius, Path.Direction.CCW)

        val centerY = (rectF.right - rectF.left) * offset + defaultPadding
        val x = rectF.right

        path.moveTo(x, centerY - triangularLength)
        path.lineTo(x + triangularLength, centerY)
        path.lineTo(x, centerY + triangularLength)
        path.close()
        canvas.drawPath(path, borderPaint)
    }

    private fun drawBottomTriangle(canvas: Canvas) {
        val triangularLength = distance
        if (triangularLength == 0f) {
            return
        }
        path.addRoundRect(rectF, radius, radius, Path.Direction.CCW)

        val centerX = (rectF.right - rectF.left) * offset + defaultPadding
        val y = rectF.bottom
        path.moveTo(centerX - triangularLength, y)
        path.lineTo(centerX, y + triangularLength)
        path.lineTo(centerX + triangularLength, y)
        path.close()
        canvas.drawPath(path, borderPaint)
    }

    fun setBackGroundColor(color: Int) {
        mBackgroundColor = color
        borderPaint.color = mBackgroundColor
        postInvalidate()
    }

    fun setEdge(@IntRange(from = 0, to = 4) direction: Int) {
        this.direction = direction
        postInvalidate()
    }

    fun setOffset(@FloatRange(from = 0.0, to = 1.0) offset: Float) {
        this.offset = offset
        postInvalidate()
    }

}