package showmethe.github.core.util.touch

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/** DSL ItemDragHelper
 * Author: showMeThe
 * Update Time: 2020/1/11 11:54
 * Package Name:showmethe.github.core.util.touch
 */
class ItemTouchDragHelper : ItemTouchHelper.Callback() {


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var dragFlags = 0
        var swipedFlags = 0

        if(dragFlag!=null){
            dragFlags = dragFlag?.invoke()!!
        }
       if(swipedFlag!=null){
           swipedFlags = swipedFlag?.invoke()!!
       }
        return makeMovementFlags(dragFlags,swipedFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return  onMove?.invoke(recyclerView, viewHolder, target)!!
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped?.invoke(viewHolder, direction)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        selectedChanged?.invoke(viewHolder, actionState)
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder): Boolean {
        if(canDropOver!=null){
            return canDropOver?.invoke(recyclerView, current, target)!!
        }
        return  super.canDropOver(recyclerView, current, target)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        clearView?.invoke(recyclerView, viewHolder)
    }

    private var  clearView: ((recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)->Unit)? = null
    fun clearView(clearView: ((recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder)->Unit)){
        this.clearView = clearView
    }

    private var  canDropOver: ((  recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)->Boolean)? = null
    fun canDropOver(canDropOver: ((recyclerView: RecyclerView, current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)->Boolean)){
        this.canDropOver = canDropOver
    }


    private var  onSwiped: ((viewHolder: RecyclerView.ViewHolder, direction: Int)->Unit)? = null
    fun onSwiped(onSwiped: ((viewHolder: RecyclerView.ViewHolder, direction: Int)->Unit)){
        this.onSwiped = onSwiped
    }

    private var  onMove: (( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)->Boolean)? = null
    fun onMove(onMove: (( recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder)->Boolean)){
        this.onMove = onMove
    }

    override fun isLongPressDragEnabled(): Boolean {
        return longPressDragEnabled
    }


    private var swipedFlag: (()->Int)? = null
    fun  makeSwipedFlags(swipedFlag: (()->Int)){
        this.swipedFlag = swipedFlag
    }

    private

    var longPressDragEnabled = true

    private var selectedChanged: ((viewHolder: RecyclerView.ViewHolder?, actionState: Int)->Unit)? = null
    fun  selectedChanged(selectedChanged: ((viewHolder: RecyclerView.ViewHolder?, actionState: Int)->Unit)){
        this.selectedChanged = selectedChanged
    }

    private var dragFlag: (()->Int)? = null
    fun  makeDragFlags(dragFlag: (()->Int)){
        this.dragFlag = dragFlag
    }

    fun dragSwiped(itemDrag: (ItemTouchDragHelper.()->Unit)?) : ItemTouchDragHelper{
        itemDrag?.invoke(this)
        return this
    }



}