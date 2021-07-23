package com.show.wanandroid.widget.overlap

import android.graphics.Color
import android.view.Gravity
import com.show.kcore.extras.display.dp
import com.show.wanandroid.widget.overlap.widget.BubbleDirection
import com.show.wanandroid.widget.overlap.widget.BubbleLayout


fun level(block: Level.() -> Unit): Level {
    val level = Level()
    block.invoke(level)
    return level
}

class Level {

    val children = ArrayList<LevelChildren>()
    var backgroundColor = Color.parseColor("#000000")

    fun backgroundColor(color: Int) {
        this.backgroundColor = color
    }

    fun child(block: LevelChildren.() -> Unit) {
        val levelChildren = LevelChildren()
        block.invoke(levelChildren)
        children.add(levelChildren)
    }

}

class LevelChildren {

    var centerX = 0f
    var centerY = 0f
    var radius = 0f
    var labelText = ""
    var labelTextGravity = Gravity.CENTER
    var labelColor = Color.parseColor("#888888")
    var labelTextColor = Color.parseColor("#000000")
    var labelWidth = 100f.dp
    var labelHeight = 100f.dp
    var edge = BubbleDirection.BOTTOM
    var offset = 0.5f

    fun labelText(text: String, edge: BubbleDirection, offset: Float = 0.5f) {
        labelText = text
        this.edge = edge
        this.offset = offset
    }

    fun labelWidthHeight(labelWidth:Float,labelHeight: Float){
        this.labelWidth = labelWidth
        this.labelHeight = labelHeight
    }

    fun labelTextGravity(gravity: Int) {
        this.labelTextGravity = gravity
    }

    fun labelColor(color: Int) {
        this.labelColor = color
    }

    fun labelTextColor(color: Int) {
        this.labelTextColor = color
    }

    fun radius(radius: Float) {
        this.radius = radius
    }

    fun center(centerX: Float, centerY: Float) {
        this.centerX = centerX
        this.centerY = centerY
    }
}