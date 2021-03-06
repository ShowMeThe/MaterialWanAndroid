package com.show.wanandroid.api

import com.ken.materialwanandroid.entity.Empty
import com.show.wanandroid.bean.*
import retrofit2.Response
import retrofit2.http.*

interface Main {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("username") username:String,@Field("password") password:String) : Response<JsonData<Auth>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend  fun register(@Field("username") username:String,@Field("password") password:String,
                          @Field("repassword") repassword:String) : Response<JsonData<Empty>>

    /**
     *  首页banner
     */
    @GET("/banner/json")
    suspend fun banner() : Response<JsonData<List<Banner>>>

    /**
     * 文章
     */
    @GET("/article/list/{pager}/json")
    suspend  fun getHomeArticle(@Path("pager") pager :Int ) : Response<JsonData<Article>>
    @GET("/article/top/json")
    suspend  fun getHomeTop() : Response<JsonData<List<DatasBean>>>


    /**
     * 文章Tab
     */
    @GET("/wxarticle/chapters/json")
    suspend fun getChapters() : Response<JsonData<List<TabBeanItem>>>
    /**
     * Tab的文章
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    suspend fun getArticle(@Path("id") id:Int,@Path("page") page:Int) : Response<JsonData<Article>>





    /**
     * 取消收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun homeUnCollect(@Path("id") id:Int): Response<JsonData<Empty>>


    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    suspend  fun homeCollect(@Path("id") id:Int): Response<JsonData<Empty>>

    /**
     * 体系数据
     */
    @GET("/tree/json")
    suspend  fun getTree() : Response<JsonData<List<Tree>>>
    @GET("/article/list/{page}/json?")
    suspend  fun getTreeArticle(@Path("page") page: Int ,@Query("cid") id: Int) : Response<JsonData<Article>>


    /**
     *  项目分类
     */
    @GET("/project/tree/json")
    suspend  fun getCateTab() : Response<JsonData<List<CateTab>>>
    /**
     *  项目列表数据
     */
    @GET("/project/list/{pager}/json")
    suspend  fun getCate(@Path("pager") pager:Int,@Query("cid") id:Int) : Response<JsonData<CateBean>>


    /**
     * 搜索热词
     */
    @GET("/hotkey/json")
    suspend fun getHotKey() : Response<JsonData<List<KeyWord>>>

    /**
     *  搜索
     */
    @FormUrlEncoded
    @POST("/article/query/{pager}/json")
    suspend  fun search(@Path("pager") pager:Int,@Field("k") keyWord:String) : Response<JsonData<Article>>


    /**
     *  收藏文章列表
     */
    @GET("/lg/collect/list/{pager}/json")
    suspend  fun getCollect(@Path("pager") pager:Int) : Response<JsonData<Collect>>
    @FormUrlEncoded
    @POST("/lg/uncollect/{id}/json")
    suspend fun unCollect(@Path("id") id:Int,@Field("originId") originId:Int): Response<JsonData<Empty>>

    /**
     * 个人积分
     */
    @GET("/lg/coin/userinfo/json")
    suspend  fun getUserInfo() : Response<JsonData<UserInfo>>


}