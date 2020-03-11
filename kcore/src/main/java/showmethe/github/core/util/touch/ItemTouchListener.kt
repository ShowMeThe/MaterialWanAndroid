package showmethe.github.core.util.touch

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * Author: showMeThe
 * Update Time: 2020/1/11 11:16
 * Package Name:showmethe.github.core.util.touch
 */
class ItemTouchListener(var rv:RecyclerView) : RecyclerView.SimpleOnItemTouchListener() {

    private var onPressInterceptTouchEvent = false
    private val touchHelper = ItemTouchHelper()
    private val gestureDetector = GestureDetector(rv.context,object : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent?) {
            onPressInterceptTouchEvent = onItemLongPress!=null
        }
    })

    companion object{
        const val SCALE_STARTED = 1
        const val SCALE_DRAGGING = 2
        const val SCALE_CANCEL = 3
    }

    private var scaleParent = true
    private var scale = 1.0f
    private var tranZ = 0f
    private var scaleView : View? = null
    private var scaleTarget : View? = null
    private var onScaleInterceptTouchEvent = false
    private val scaleTouch = ItemScaleHelper()
    private val scaleGestureDetector = ScaleGestureDetector(rv.context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
            onScaleInterceptTouchEvent = true
            rv.parent.requestDisallowInterceptTouchEvent(true)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleView?.apply {
                if(scaleParent){
                    scaleTarget = this
                    makeScale(detector)
                }else{
                    if(this is ViewGroup){
                    for(i in 0..childCount){
                        scaleTarget = getChildAt(i)
                        if(scaleTarget is ImageView){
                            makeScale(detector)
                            break
                        }else if(scaleTarget is ViewGroup){
                            scaleTarget = findFirstImageView(scaleTarget as ViewGroup)
                            makeScale(detector)
                            break
                        }
                    }
                }
                }
            }
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            scaleTarget?.apply {
                rv.clipChildren = true
                rv.clipToPadding = true
                scaleTouch.downX = 0f
                scaleTouch.downY = 0f
                animate()
                    .setListener(listener)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .translationZ(tranZ)
                    .translationY(0f)
                    .translationX(0f)
                    .setDuration(150).start()
            }
        }
    })

    private fun makeScale(detector: ScaleGestureDetector){
        rv.clipChildren = false
        rv.clipToPadding = false
        scaleTarget?.apply {
            scale *= detector.scaleFactor
            scaleX = scale
            scaleY = scale
            onItemScale?.invoke(scaleTarget,scale, SCALE_DRAGGING)
        }
    }

    private fun findFirstImageView(childViewGroup: ViewGroup?) : ImageView?{
        if(childViewGroup!=null){
            for(i in 0..childViewGroup.childCount){
                val  view = childViewGroup.getChildAt(i)
                if( view is ImageView){
                    return view
                }else if(view is ViewGroup){
                    return findFirstImageView(view)
                }
            }
        }
        return null
    }

    private val listener =  object   : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            onScaleInterceptTouchEvent = false
            rv.parent.requestDisallowInterceptTouchEvent(false)
            onItemScale?.invoke(scaleTarget,1.0f, SCALE_CANCEL)
        }
    }


    init {
        touchHelper.setOnItemTouchListener { isTouchChild, childView, childPosition, event ->
            if(onItemLongPress!=null){
                rv.parent.requestDisallowInterceptTouchEvent(isTouchChild)
                onPressInterceptTouchEvent = isTouchChild
                onItemLongPress?.invoke(isTouchChild, childView, childPosition)
            }
        }

        scaleTouch.setOnItemScaleListener { isTouchChild, childView, childPosition, event ->
            if(onItemScale != null ){
                if(isTouchChild){
                    onItemScale?.invoke(childView,1.0f,SCALE_STARTED)
                    scaleView = childView?.apply {
                        scale = scaleX
                        tranZ = translationZ
                        translationZ = tranZ + 1
                    }
                }
                onScaleInterceptTouchEvent = isTouchChild
            }
        }
    }


    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(e)
        scaleTouch.onTouEvent(rv,e)

        return (onPressInterceptTouchEvent || onScaleInterceptTouchEvent)
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        touchHelper.onTouEvent(rv,e)
        scaleGestureDetector.onTouchEvent(e)

        if(onScaleInterceptTouchEvent){
            when(e.action){
                MotionEvent.ACTION_MOVE->{
                    scaleView?.translationX = e.x - scaleTouch.downX
                    scaleView?.translationY = e.y - scaleTouch.downY
                }
            }
        }
    }

    /**
     * RecyclerView Item 长按
     */
    private var onItemLongPress : ((isTouchChild: Boolean, childView: View?, childPosition: Int)->Unit)? = null
    fun setOnItemLongPressing(onItemLongPress : ((isTouchChild: Boolean, childView: View?, childPosition: Int)->Unit)){
        this.onItemLongPress = onItemLongPress
    }

    /**
     * RecyclerView onItemScale true 时候缩放Item,false 缩放Item第一个ImageView
     */
    private var onItemScale : ((childView: View?, scale: Float,state:Int)->Unit)? = null
    fun setOnItemScaling(scaleParent:Boolean = true,onItemScale : ((childView: View?, scale: Float,state:Int)->Unit)){
        this.scaleParent = scaleParent
        this.onItemScale = onItemScale
    }


}

