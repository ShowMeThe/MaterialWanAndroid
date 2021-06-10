package com.show.wanandroid.widget.swipe

class SwipeBuilder {

    companion object {
        fun builder() = SwipeBuilder()
    }

    private val menus = ArrayList<Menu>()

    fun addMenu(menu: Menu): SwipeBuilder {
        menus.add(menu)
        return this
    }

    fun build() = menus

}