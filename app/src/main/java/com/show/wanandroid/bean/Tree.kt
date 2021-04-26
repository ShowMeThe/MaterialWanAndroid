package com.show.wanandroid.bean

import kotlin.collections.ArrayList


class Tree {
    /**
     * children : [{"children":[],"courseId":13,"id":60,"name":"Android Studio相关","order":1000,"parentChapterId":150,"visible":1}]
     * courseId : 13
     * id : 150
     * name : 开发环境
     * order : 1
     * parentChapterId : 0
     * visible : 1
     */
    var courseId = 0
    var id = 0
    var name = ""
    var order = 0
    var parentChapterId = 0
    var visible = 0
    var children: ArrayList<ChildrenBean> = ArrayList()



    class ChildrenBean {
        /**
         * children : []
         * courseId : 13
         * id : 60
         * name : Android Studio相关
         * order : 1000
         * parentChapterId : 150
         * visible : 1
         */
        var courseId = 0
        var id = 0
        var name = ""
        var order = 0
        var parentChapterId = 0
        var visible = 0
        var children: List<*>? = null
    }


}