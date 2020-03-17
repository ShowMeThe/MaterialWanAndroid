package com.show.wanandroid.api

import com.ken.materialwanandroid.entity.Empty
import com.show.wanandroid.entity.Article
import com.show.wanandroid.entity.Auth
import com.show.wanandroid.entity.Banner
import retrofit2.Response
import retrofit2.http.*
import showmethe.github.core.http.JsonResult

interface Main {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username:String,@Field("password") password:String) : Response<JsonResult<Auth>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend  fun register(@Field("username") username:String,@Field("password") password:String,
                          @Field("repassword") repassword:String) : Response<JsonResult<Empty>>

    /**
     *  首页banner
     */
    @GET("/banner/json")
    suspend  fun banner() : Response<JsonResult<ArrayList<Banner>>>


    /**
     * 文章
     */
    @GET("/article/list/{pager}/json")
    suspend  fun getHomeArticle(@Path("pager") pager :Int ) : Response<JsonResult<Article>>


    /**
     * 取消收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun homeUnCollect(@Path("id") id:Int): Response<JsonResult<Empty>>


    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    suspend  fun homeCollect(@Path("id") id:Int): Response<JsonResult<Empty>>
}