package com.yx.framework

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by yangxiong on 2020/6/9.
 */
interface TestService {
    @GET("search/repositories")
    suspend fun getDate(@Query("q") query: String,
                                  @Query("sort") sort: String = "stars"):JsonObject
}