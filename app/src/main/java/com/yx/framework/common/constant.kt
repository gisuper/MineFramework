package com.yx.framework.common

import com.yx.framework.mvvm.model.api.UserService
import com.yx.framework.net.RetrofitManager

/**
 * Created by yangxiong on 2020/6/9.
 */
val HOST_URL = "https://www.wanandroid.com/"
val CONNECT_TIMEOUT = 20L
val READ_TIMEOUT = 20L
val WRITE_TIMEOUT = 20L

val userServiceImpl = RetrofitManager.instance.retrofit.create(UserService::class.java)