package com.show.wanandroid.entity

import java.util.*
import kotlin.collections.ArrayList

class Collect {
    /**
     * curPage : 2
     * datas : [{"author":"包牙齿","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85020,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857315000,"title":"最近给团队新同学分享的git markdown","userId":30077,"visible":0,"zan":0},{"author":"xjz729827161","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85019,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857314000,"title":"Gradle工作原理全面了解","userId":30077,"visible":0,"zan":0},{"author":"ZYLAB","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85018,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857313000,"title":"【Android 修炼手册】Gradle 篇 -- Android Gradle Plugin 主要 Task 分析","userId":30077,"visible":0,"zan":0},{"author":"heqiangfly","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85016,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857278000,"title":"Gradle 使用指南 -- Gradle 生命周期","userId":30077,"visible":0,"zan":0},{"author":"包牙齿","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85015,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857275000,"title":"最近给团队新同学分享的git markdown","userId":30077,"visible":0,"zan":0},{"author":"xjz729827161","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85014,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857272000,"title":"Gradle工作原理全面了解","userId":30077,"visible":0,"zan":0},{"author":"ZYLAB","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85013,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857269000,"title":"【Android 修炼手册】Gradle 篇 -- Android Gradle Plugin 主要 Task 分析","userId":30077,"visible":0,"zan":0},{"author":"ZYLAB","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85012,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857255000,"title":"【Android 修炼手册】Gradle 篇 -- Android Gradle Plugin 主要 Task 分析","userId":30077,"visible":0,"zan":0},{"author":"包牙齿","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85009,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857152000,"title":"最近给团队新同学分享的git markdown","userId":30077,"visible":0,"zan":0},{"author":"包牙齿","chapterId":0,"chapterName":"","courseId":13,"desc":"","envelopePic":"","id":85008,"link":"","niceDate":"1天前","origin":"","originId":-1,"publishTime":1567857131000,"title":"最近给团队新同学分享的git markdown","userId":30077,"visible":0,"zan":0}]
     * offset : 20
     * over : true
     * pageCount : 2
     * size : 20
     * total : 30
     */
    var curPage = 0
    var offset = 0
    var isOver = false
    var pageCount = 0
    var size = 0
    var total = 0
    var datas: ArrayList<DatasBean> = ArrayList()

    class DatasBean {
        /**
         * author : 包牙齿
         * chapterId : 0
         * chapterName :
         * courseId : 13
         * desc :
         * envelopePic :
         * id : 85020
         * link :
         * niceDate : 1天前
         * origin :
         * originId : -1
         * publishTime : 1567857315000
         * title : 最近给团队新同学分享的git markdown
         * userId : 30077
         * visible : 0
         * zan : 0
         */
        var imgWidth = 0
        var imgHeight = 0
        var author= ""
        var chapterId = 0
        var chapterName= ""
        var courseId = 0
        var desc= ""
        var envelopePic = ""
        var id = 0
        var link= ""
        var niceDate= ""
        var origin= ""
        var originId = 0
        var publishTime  = 0L
        var title= ""
        var userId = 0
        var visible = 0
        var zan = 0

    }
}