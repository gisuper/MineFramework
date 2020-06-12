package com.yx.framework.mvvm.model.api

import com.yx.framework.mvvm.model.bean.CoinBean
import com.yx.framework.mvvm.model.bean.UserBean
import retrofit2.http.*

/**
 * Created by yangxiong on 2020/6/9.
 */
interface UserService {
    //登录
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): UserBean
    //注册
    @POST("user/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): UserBean
    //退出
    @GET("user/logout/json")
    suspend fun logout(): CoinBean



    //获取个人积分
    @GET("lg/coin/userinfo/json")
    suspend fun coin(): CoinBean



}