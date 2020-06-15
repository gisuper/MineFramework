package com.yx.framework.mvvm.model.repository

import com.yx.framework.mvvm.model.api.Response
import com.yx.framework.mvvm.model.api.UserService
import com.yx.framework.mvvm.model.bean.CoinBean
import com.yx.framework.mvvm.model.bean.UserBean
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by yangxiong on 2020/6/10.
 */
@Singleton
class UserRepository @Inject constructor(val userServiceIml: UserService) : BaseRepository() {

    val TAG = "UserRepository"
    suspend fun register(
        username: String,
        password: String,
        repassword: String
    ): Response<UserBean> {
        return excute { userServiceIml.register(username, password, repassword) }
    }

    suspend fun login(username: String, password: String): Response<UserBean> {
        return excute { userServiceIml.login(username, password) }
    }

    suspend fun coin(): Response<CoinBean> {
        return excute { userServiceIml.coin() }
    }

    suspend fun logout(): Response<CoinBean> {
        return excute { userServiceIml.logout() }
    }
}