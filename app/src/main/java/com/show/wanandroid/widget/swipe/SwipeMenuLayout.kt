package com.show.wanandroid.widget.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import kotlin.math.abs
import kotlin.math.sqrt

class SwipeMenuLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val INVALID_POINTER = -1
    }


    private val config by lazy { ViewConfiguration.get(getContext()) }
    private val mOverScrollDistance by lazy { config.scaledOverscrollDistance }
    private val mTouchSlop by lazy { config.scaledTouchSlop }
    private val mScaledMinimumFlingVelocity by lazy { config.scaledMinimumFlingVelocity }
    private val mScaledMaximumFlingVelocity by lazy { config.scaledMaximumFlingVelocity }

    private var mVelocityTracker: VelocityTracker? = null

    var contentView: View? = null
    var leftMenuView: LinearLayout? = null

    private var mLastX = 0f
    private var mDownX = 0f
    private var mDownY = 0f
    private var deltaX = 0f

    private var isDragging = false

    private var mActivePointerId = INVALID_POINTER

    private val mScroller by lazy { OverScroller(context) }


    private var onSwipeChange: ((isOpen: Boolean) -> Unit)? = null
    fun setOnSwipeChangeListener(onSwipeChange: ((isOpen: Boolean) -> Unit)) {
        this.onSwipeChange = onSwipeChange
    }

    init {
        isClickable = true
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (!clickInContent(event)) {
            return false
        }

        if (super.onInterceptTouchEvent(event)) {
            return true
        }

        kotlin.runCatching {
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_MOVE -> {
                    val x = event.getX(mActivePointerId)
                    val deltaX = mLastX - event.x
                    val deltaY = mDownY - event.y
                    val activePointerId: Int = mActivePointerId
                    if (activePointerId != INVALID_POINTER && abs(deltaX) > mTouchSlop && abs(deltaX) > abs(deltaY)) {
                        mLastX = x
                        isDragging = true
                        parent?.requestDisallowInterceptTouchEvent(true)

                        initVelocityTrackerIfNull()
                        mVelocityTracker?.addMovement(event)
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    mLastX = event.x;
                    mActivePointerId = event.getPointerId(0)

                    mDownX = event.x
                    mDownY = event.y

                    isDragging = !mScroller.isFinished
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    isDragging = false
                    mActivePointerId = INVALID_POINTER

                    if (!mScroller.isFinished) mScroller.abortAnimation()
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    val index = event.actionIndex;
                    mLastX = event.getX(index)
                    mActivePointerId = event.getPointerId(index)
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    mLastX = event.getX(event.findPointerIndex(mActivePointerId))
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
        return isDragging
    }

    override fun shouldDelayChildPressedState(): Boolean {
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        kotlin.runCatching {
            initVelocityTrackerIfNull()
            mVelocityTracker?.addMovement(event)
            val action = event.action
            when (action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    if (childCount == 0) {
                        return false
                    }
                    mLastX = event.x

                    mDownX = event.x
                    mDownY = event.y

                    if (!mScroller.isFinished) {
                        mScroller.abortAnimation()
                    }

                    mActivePointerId = event.getPointerId(0)
                }
                MotionEvent.ACTION_MOVE -> {
                    val activePointerIndex = event.findPointerIndex(mActivePointerId)
                    if (activePointerIndex != INVALID_POINTER) {
                        val x = event.getX(activePointerIndex)

                        deltaX = mLastX - x
                        val deltaY = mDownY - event.y

                        if (!isDragging && abs(deltaX) > mTouchSlop && abs(deltaX) > abs(deltaY)) {
                            parent?.requestDisallowInterceptTouchEvent(true)
                            isDragging = true

                            if (deltaX > 0) {
                                deltaX -= mTouchSlop
                            } else {
                                deltaX += mTouchSlop
                            }
                        }

                        if (isDragging) {
                            mLastX = x

                            if (overScrollBy(
                                    deltaX.toInt(), 0, scrollX, 0, getScrollRange(),
                                    0, mOverScrollDistance, 0, true
                                )
                            ) {
                                mVelocityTracker?.clear()
                            }

                        }
                    }
                }
                MotionEvent.ACTION_CANCEL ->{
                    isDragging = false
                    mActivePointerId = INVALID_POINTER

                    mVelocityTracker?.clear()
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null

                }
                MotionEvent.ACTION_UP, -> {
                    isDragging = false
                    mActivePointerId = INVALID_POINTER

                    mVelocityTracker?.computeCurrentVelocity(
                        1000,
                        mScaledMaximumFlingVelocity.toFloat()
                    )
                    val velocityX = mVelocityTracker?.xVelocity ?: mScaledMinimumFlingVelocity.toFloat()

                    val duration = getDuration()

                    val offsetX = mDownX - event.x
                    val offsetY = mDownY - event.y
                    val motionDistant = abs(sqrt(offsetX * offsetX + offsetY * offsetY))
                    if (motionDistant > mTouchSlop) {
                        if (velocityX > 0) {
                            // smooth close
                            smoothCloseMenu(duration)
                        } else if (velocityX < 0) {
                            // smooth open
                            smoothOpenMenu(duration)
                        } else {
                            val legalDistant = (leftMenuView?.width?.toFloat() ?: 0f) / 2f
                            if (scrollX.toFloat() >= (legalDistant)) {
                                // smooth open
                                smoothOpenMenu(duration)
                            } else {
                                // smooth close
                                smoothCloseMenu(duration)
                            }
                        }
                        ViewCompat.postInvalidateOnAnimation(this)
                    } else if (clickInContent(event)) {
                        performClick()
                    }
                    mVelocityTracker?.clear()
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
        return true
    }


    private fun clickInContent(event: MotionEvent): Boolean {
        var isClickInChild = false
        val offsetX = scrollX
        contentView?.apply {
            isClickInChild = event.x >= left - offsetX
                    && event.x <= right - offsetX
                    && event.y >= top
                    && event.y <= bottom
        }
        return isClickInChild
    }

    fun openMenu() {
        val scroll = (leftMenuView?.right ?: 0) - (leftMenuView?.left ?: 0) - abs(scrollX)
        scrollTo(scroll, 0)
    }

    fun closeMenu() {
        scrollTo(0, 0)
    }

    fun smoothCloseMenu(duration: Int = getDuration(),notifyChange:Boolean = true) {
        if(notifyChange){
            onSwipeChange?.invoke(false)
        }
        mScroller.startScroll(scrollX, 0, -abs(scrollX), 0, duration)
        if(notifyChange.not()){
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun smoothOpenMenu(duration: Int = getDuration(),notifyChange:Boolean = true) {
        if(notifyChange){
            onSwipeChange?.invoke(true)
        }
        mScroller.startScroll(
            abs(scrollX),
            0,
            ((leftMenuView?.right ?: 0) - (leftMenuView?.left ?: 0)) - abs(scrollX),
            0,
            duration
        )
        if(notifyChange.not()){
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(abs(mScroller.currX), 0)
            postInvalidate()
        }
    }


    private fun getDuration(): Int {
        return 500
    }

    private fun initVelocityTrackerIfNull() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }


    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        super.scrollTo(scrollX, scrollY)
    }


    private fun getScrollRange(): Int {
        var scrollRange = 0
        leftMenuView?.apply {
            scrollRange = width
        }
        return scrollRange
    }

    private fun findViewWhenNull(){
        if(contentView == null){
            contentView = getChildAt(0)
        }
        if(leftMenuView == null){
            leftMenuView = findViewWithTag("leftMenu")
        }
    }


    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        findViewWhenNull()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        findViewWhenNull()

        var topPadding = 0
        var bottomPadding = 0
        var rightPadding = 0
        var contentWidth = 0
        contentView?.apply {
            val lp = layoutParams as LayoutParams
            val leftPadding = paddingLeft + lp.leftMargin
            rightPadding = paddingRight + lp.rightMargin
            topPadding = paddingTop + lp.topMargin
            bottomPadding = paddingBottom + lp.bottomMargin

            contentWidth = this@SwipeMenuLayout.measuredWidth - rightPadding
            layout(
                leftPadding,
                topPadding,
                contentWidth,
                this@SwipeMenuLayout.measuredHeight - bottomPadding
            )
        }

        leftMenuView?.apply {
            val menuWidth = measuredWidthAndState
            val start = contentWidth + rightPadding
            layout(
                start,
                topPadding,
                start + menuWidth,
                this@SwipeMenuLayout.measuredHeight - bottomPadding
            )
        }
    }


}