package com.show.wanandroid.api

import com.show.wanandroid.entity.Banner
import retrofit2.Response
import retrofit2.http.GET
import showmethe.github.core.http.JsonResult

interface Main {

    /**
     *  首页banner
     */
    @GET("/banner/json")
    suspend  fun banner() : Response<JsonResult<ArrayList<Banner>>>

}