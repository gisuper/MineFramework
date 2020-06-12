package com.yx.framework.mvvm.model.repository

import android.util.Log
import com.yx.framework.common.userServiceImpl
import com.yx.framework.mvvm.model.api.Response
import com.yx.framework.mvvm.model.bean.CoinBean
import com.yx.framework.mvvm.model.bean.UserBean
import retrofit2.HttpException

/**
 * Created by yangxiong on 2020/6/10.
 */
class UserRepository : BaseRepository() {
    val TAG = "UserRepository"
    suspend fun register(
        username: String,
        password: String,
        repassword: String
    ): Response<UserBean> {
        return excute { userServiceImpl.register(username, password, repassword) }
    }

    suspend fun login(username: String, password: String): Response<UserBean> {
        return excute { userServiceImpl.login(username, password) }
    }
    suspend fun coin(): Response<CoinBean> {
        return excute { userServiceImpl.coin() }
    }


}