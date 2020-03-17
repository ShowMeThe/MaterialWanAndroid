package com.show.wanandroid.entity

import java.util.*
import kotlin.collections.ArrayList

class Article {
    /**
     * curPage : 1
     * datas : [{"apkLink":"","author":"鸿洋","chapterId":408,"chapterName":"鸿洋","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":9038,"link":"https://mp.weixin.qq.com/s/BIfAYbqC9EOyXy7fwU1LNg","niceDate":"2019-08-27","origin":"","prefix":"","projectLink":"","publishTime":1566835200000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/408/1"}],"title":"Android 值得深入思考面试问答分享 | 5","type":0,"userId":-1,"visible":1,"zan":0}]
     */
    var curPage = 0
    var datas: ArrayList<DatasBean> = ArrayList()

    class DatasBean {
        /**
         * apkLink :
         * author : 鸿洋
         * chapterId : 408
         * chapterName : 鸿洋
         * collect : false
         * courseId : 13
         * desc :
         * envelopePic :
         * fresh : false
         * id : 9038
         * link : https://mp.weixin.qq.com/s/BIfAYbqC9EOyXy7fwU1LNg
         * niceDate : 2019-08-27
         * origin :
         * prefix :
         * projectLink :
         * publishTime : 1566835200000
         * superChapterId : 408
         * superChapterName : 公众号
         * tags : [{"name":"公众号","url":"/wxarticle/list/408/1"}]
         * title : Android 值得深入思考面试问答分享 | 5
         * type : 0
         * userId : -1
         * visible : 1
         * zan : 0
         */
        var apkLink: String = ""
        var author: String = ""
        var chapterId = 0
        var chapterName: String = ""
        var isCollect = false
        var courseId = 0
        var desc: String = ""
        var envelopePic: String = ""
        var isFresh = false
        var id = 0
        var link: String = ""
        var niceDate: String = ""
        var origin:String = ""
        var prefix: String = ""
        var projectLink: String = ""
        var publishTime: Long = 0
        var superChapterId = 0
        var superChapterName: String = ""
        var title: String = ""
        var type = 0
        var userId = 0
        var visible = 0
        var zan = 0
        var tags: List<TagsBean>? = null
        var shareUser: String = ""

        class TagsBean {
            /**
             * name : 公众号
             * url : /wxarticle/list/408/1
             */
            var name: String = ""
            var url: String = ""

        }
    }
}