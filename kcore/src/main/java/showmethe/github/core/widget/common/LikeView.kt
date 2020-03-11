package showmethe.github.core.widget.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.*
import showmethe.github.core.R
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin


/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:51
 * Package Name:showmethe.github.core.widget.common
 */
class LikeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val DOTS_COUNT = 8
    private val DOTS_POSITION_ANGLE = 360 / DOTS_COUNT
    private var colors : Array<Int>? = null
    private var currentProgress = 0f
    private var centerX: Int = 0
    private var centerY: Int = 0
    private var maxDotSize: Float = 6.toFloat()
    private var currentRadius = 0f
    private var currentDotSize = 0f
    private val circlePaint = Paint()
    private val bitmapPaint = Paint()
    private var maxDotsRadius: Float = 0.toFloat()
    private var  dotsAnimator : ObjectAnimator? = null
    private var  scaleAnimator : ObjectAnimator? = null
    private val rect=  Rect()
    private lateinit var like : Bitmap
    private lateinit var unlike : Bitmap
    private var mHeight = 0
    private var mWidth = 0
    var isLike = false
    private val set  =  AnimatorSet()
    private var percentage = 0.35f
    private var widthDis =0
    private var heightDis =0
    private var likeImg  = -1
    private var unLikeImg  = -1

    init {
        init(context,attrs)
    }

    private fun init(context: Context,attrs: AttributeSet?) {
        colors = arrayOf(Color.parseColor("#f44336"))//颜色数组修改
        circlePaint.style = Paint.Style.FILL
        initAnim()
        initType(context,attrs)
    }

    private fun initType(context: Context, attrs: AttributeSet?) {
        val arrary = context.obtainStyledAttributes(attrs!!,R.styleable.LikeView)
        isLike = arrary.getBoolean(R.styleable.LikeView_like_state,false)
        likeImg = arrary.getResourceId(R.styleable.LikeView_like,-1)
        unLikeImg =  arrary.getResourceId(R.styleable.LikeView_unLike,-1)
        percentage = arrary.getFloat(R.styleable.LikeView_percentage,0.35f)

        like = BitmapFactory.decodeResource(resources, likeImg)
        unlike = BitmapFactory.decodeResource(resources,unLikeImg)
        arrary.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        setLike(isLike,false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        //粒子半径
        maxDotSize = 5f
        //最大半径
        maxDotsRadius = w / 2 - maxDotSize * 4

        widthDis = mWidth/2
        heightDis = mHeight/2


    }

    override fun onDraw(canvas: Canvas) {
        drawOuterDotsFrame(canvas)


        val left = min(like.width/2f,widthDis/2f)
        val bottom = min(like.height/2f,widthDis/2f)

        rect.set((widthDis - left).toInt(), (heightDis - bottom).toInt(), (widthDis + left).toInt(),
            (heightDis + bottom).toInt()
        )
        if(isLike){
            canvas.drawBitmap(like,null,rect,bitmapPaint)
        }else{
            canvas.drawBitmap(unlike,null,rect,bitmapPaint)
        }
    }

    fun setLike(boolean: Boolean,state: Boolean){
        isLike = boolean
            if(state){
                if(isLike){
                    play()
                }else{
                    percentage = 0.5f
                    playScale()
                }
        }
        postInvalidate()
    }

    private fun drawOuterDotsFrame(canvas: Canvas) {
        for (i in 0 until DOTS_COUNT) { //小红点会绕中心旋转
            val cX = (centerX + currentRadius * cos((i+1*percentage*1.5) * DOTS_POSITION_ANGLE * Math.PI / 180)) .toFloat()
            val cY = (centerY + currentRadius * sin((i+1*percentage*1.5)* DOTS_POSITION_ANGLE * Math.PI / 180)).toFloat()
            circlePaint.color = colors!![0] //固定了一个颜色
            if(i%2 == 0){ //奇偶大小变化
                canvas.drawCircle(cX, cY, currentDotSize, circlePaint)
            }else{
                canvas.drawCircle(cX, cY, (currentDotSize*0.6).toFloat(), circlePaint)
            }
        }
    }

    private fun setCurrentProgress(currentProgress: Float) {
        this.currentProgress = currentProgress
        updateOuterDotsPosition()
        postInvalidate()
    }

    private fun setPercentage(percentage: Float) {
        this.percentage = percentage
        if(!isLike){
            postInvalidate()
        }
    }

    private fun updateOuterDotsPosition() {
        if (currentProgress < 0.3f) {
            this.currentRadius = mapValueFromRangeToRange(currentProgress, 0.0f, 0.3f, 0.0f, maxDotsRadius * 0.8f)
        } else {
            this.currentRadius =
                mapValueFromRangeToRange(currentProgress, 0.3f, 1.0f, 0.8f * maxDotsRadius, maxDotsRadius)
        }
        if (currentProgress < 0.7) {
            this.currentDotSize = maxDotSize
        } else {
            this.currentDotSize = mapValueFromRangeToRange(currentProgress, 0.7f, 1.0f, maxDotSize, 0.0f)
        }
    }

    private fun mapValueFromRangeToRange(value: Float, fromLow: Float, fromHigh: Float, toLow: Float, toHigh: Float): Float {
        return toLow + (value - fromLow) / (fromHigh - fromLow) * (toHigh - toLow)
    }

   private fun initAnim(){
       dotsAnimator = ObjectAnimator.ofFloat(this, "currentProgress", 0f, 1f)
       scaleAnimator = ObjectAnimator.ofFloat(this, "percentage", 0.1f, percentage)
       set.playTogether(dotsAnimator,scaleAnimator)
       set.duration = 650
       set.interpolator = AccelerateDecelerateInterpolator()
   }

    private fun playScale(){
        scaleAnimator = ObjectAnimator.ofFloat(this, "percentage", 0.1f, percentage)
        scaleAnimator!!.duration = 300
        scaleAnimator!!.interpolator = AccelerateDecelerateInterpolator()
        scaleAnimator!!.start()
    }

   private fun play(){
        if(set.isStarted || set.isRunning){
            return
        }
        set.start()
    }

}