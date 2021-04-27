package com.show.wanandroid.bean

import kotlin.collections.ArrayList
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json





@JsonClass(generateAdapter = true)
data class Tree(
    @Json(name = "children")
    val children: List<Children>,
    @Json(name = "courseId")
    val courseId: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "order")
    val order: Int,
    @Json(name = "parentChapterId")
    val parentChapterId: Int,
    @Json(name = "userControlSetTop")
    val userControlSetTop: Boolean,
    @Json(name = "visible")
    val visible: Int
)

@JsonClass(generateAdapter = true)
data class Children(
    @Json(name = "children")
    val children: List<Any>,
    @Json(name = "courseId")
    val courseId: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "order")
    val order: Int,
    @Json(name = "parentChapterId")
    val parentChapterId: Int,
    @Json(name = "userControlSetTop")
    val userControlSetTop: Boolean,
    @Json(name = "visible")
    val visible: Int
)