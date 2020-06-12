package com.yx.framework.mvvm.model.api

import java.lang.Exception

/**
 * Created by yangxiong on 2020/6/12.
 */
data class Response<T>(val code: Int = 0, val msg: String = "success", val data: T)