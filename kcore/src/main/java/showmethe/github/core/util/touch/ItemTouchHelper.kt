package showmethe.github.core.util.touch

import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class ItemTouchHelper{

    private var onItemTouchListener : ((isTouchChild: Boolean, childView: View?, childPosition: Int, event: MotionEvent?)->Unit)? = null
    fun setOnItemTouchListener(onItemTouchListener : ((isTouchChild: Boolean, childView: View?, childPosition: Int, event: MotionEvent?)->Unit)){
        this.onItemTouchListener = onItemTouchListener
    }

    fun onTouEvent(rv:RecyclerView,ev:MotionEvent) : Boolean{

        val childView = rv.findChildViewUnder(ev.x,ev.y) //坐标获取childView
        var position = -1
        childView?.let { position = rv.getChildAdapterPosition(childView) }//获取position
        if(childView == null || position == -1){
            onItemTouchListener?.invoke(false,null,-1,ev)
        }
        if (ev.action == MotionEvent.ACTION_DOWN || ev.action == MotionEvent.ACTION_MOVE) {
            if (onItemTouchListener != null) {
                onItemTouchListener?.invoke(true, childView, position, ev)
                return true
            }
        } else if (ev.action == MotionEvent.ACTION_UP) {
            if (onItemTouchListener != null) {
                onItemTouchListener?.invoke(false, childView, position, ev)
                return true
            }
        }

        return false
    }

}