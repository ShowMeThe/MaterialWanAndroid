package com.show.kcore.adapter.divider

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *  默认分割线：高度为1px，颜色为灰色
 *  列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
 */
class RecycleViewDivider(vararg  orientation: Int, val dividerHeight: Int = 1,
                         var padding:Float = 0f,
                         dividerColor: Int = Color.LTGRAY) : RecyclerView.ItemDecoration() {

    private val  mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var  orientations = IntArray(2)

    init {
        mPaint.color = dividerColor
        mPaint.style = Paint.Style.FILL
        orientations =  orientation.copyOf()
    }

    //获取分割线尺寸
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if(orientations.size == 1){
            if (orientations[0] == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, 0, dividerHeight)
            } else if (orientations[0] == LinearLayoutManager.HORIZONTAL){
                outRect.set(0, 0, dividerHeight, 0)
            }
        }else{
            if(orientations.contains(LinearLayoutManager.VERTICAL) && orientations.contains(LinearLayoutManager.HORIZONTAL)){
                outRect.set(dividerHeight/2, dividerHeight/2, dividerHeight/2, dividerHeight/2)
            }
        }
    }

    //绘制分割线
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if(orientations.size == 1){
            if (orientations[0] == LinearLayoutManager.VERTICAL) {
                drawVertical(c, parent)
            } else {
                drawHorizontal(c, parent)
            }
        }else{
            if(orientations.contains(LinearLayoutManager.VERTICAL) && orientations.contains(LinearLayoutManager.HORIZONTAL)){
                drawHalfHorizontal(c,parent)
                drawHalfVertical(c,parent)
            }
        }

    }

    private fun drawHalfHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop + padding/2f
        val bottom = parent.measuredHeight - parent.paddingBottom - padding/2f
        val childSize = parent.childCount
        for (i in 0 until childSize ) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            val left = child.right + layoutParams.rightMargin
            val right = left + dividerHeight/2
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)

            val left2 = child.left - layoutParams.leftMargin
            val right2 = left2 - dividerHeight/2
            canvas.drawRect(left2.toFloat(), top.toFloat(), right2.toFloat(), bottom.toFloat(), mPaint)
        }
    }

    private fun drawHalfVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + padding/2f
        val right = parent.measuredWidth - parent.paddingRight - + padding/2f
        val childSize = parent.childCount
        for (i in 0 until childSize - 1  ) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + dividerHeight/2
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)

            val top2 = child.top - layoutParams.topMargin
            val bottom2 = top2 - dividerHeight/2
            canvas.drawRect(left.toFloat(), top2.toFloat(), right.toFloat(), bottom2.toFloat(), mPaint)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop + padding
        val bottom = parent.measuredHeight - parent.paddingBottom - padding
        val childSize = parent.childCount
        for (i in 0 until childSize ) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + dividerHeight
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + padding
        val right = parent.measuredWidth - parent.paddingRight - padding
        val childSize = parent.childCount
        for (i in 0 until childSize - 1  ) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + dividerHeight
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }
}
