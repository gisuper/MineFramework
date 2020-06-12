package com.yx.framework.mvvm.model.repository

import android.util.Log
import com.yx.framework.mvvm.model.api.Response
import com.yx.framework.mvvm.model.bean.UserBean
import retrofit2.HttpException

/**
 * Created by yangxiong on 2020/6/12.
 */
open class BaseRepository {
    suspend fun <T> excute(request: suspend () -> T): Response<T> {
        try {
            return Response(data = request())
        } catch (e: Exception) {
            when (e) {
                is NullPointerException, is Throwable, is HttpException -> {//body == null,未知错误
                    return Response(
                        code = -1,
                        msg = e.message.toString(),
                        data = null
                    ) as Response<T>
                }
                else -> {//正常不会走到这里
                    return Response(code = -2, msg = "不可预知错误", data = null) as Response<T>
                }
            }
            e.printStackTrace()
        }
    }
}