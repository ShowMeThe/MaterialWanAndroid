package com.show.wanandroid.bean
import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json



@JsonClass(generateAdapter = true)
data class Banner(
    @Json(name = "desc")
    val desc: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "imagePath")
    val imagePath: String,
    @Json(name = "isVisible")
    val isVisible: Int,
    @Json(name = "order")
    val order: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: Int,
    @Json(name = "url")
    val url: String
)