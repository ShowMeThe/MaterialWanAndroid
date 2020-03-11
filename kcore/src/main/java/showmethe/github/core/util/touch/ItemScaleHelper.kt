package showmethe.github.core.util.touch

import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class ItemScaleHelper{

    var downX =0f
    var downY = 0f

    private var onItemScaleListener : ((isTouchChild: Boolean, childView: View?, childPosition: Int, event: MotionEvent?)->Unit)? = null
    fun setOnItemScaleListener(onItemScaleListener : ((isTouchChild: Boolean, childView: View?, childPosition: Int, event: MotionEvent?)->Unit)){
        this.onItemScaleListener = onItemScaleListener
    }

    fun onTouEvent(rv: RecyclerView, ev: MotionEvent) : Boolean{

        val childView = rv.findChildViewUnder(ev.x,ev.y) //坐标获取childView
        var position = -1
        childView?.let { position = rv.getChildAdapterPosition(childView) }//获取position
        if(childView == null || position == -1){
            onItemScaleListener?.invoke(false,null,-1,ev)
        }

        when(ev.action){
             MotionEvent.ACTION_DOWN,MotionEvent.ACTION_MOVE ->{
                if (onItemScaleListener != null) {
                    if(ev.action == MotionEvent.ACTION_DOWN){
                        downX = ev.x
                        downY = ev.y
                    }
                    if(ev.pointerCount == 2){ //仅双指放大
                        onItemScaleListener?.invoke(true,childView, position, ev)
                    }
                    return true
                }
            }
           MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL  ->{
                if (onItemScaleListener != null) {
                    onItemScaleListener?.invoke(false,childView, position, ev)
                }
               return true
            }
        }
        return false
    }

}