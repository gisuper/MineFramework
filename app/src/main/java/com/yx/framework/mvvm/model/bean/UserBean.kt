package com.yx.framework.mvvm.model.bean

data class UserBean(val data: UserData,
                    val errorCode: Int = 0,
                    val errorMsg: String = "")
data class UserData(val password: String = "",
                val publicName: String = "",
                val icon: String = "",
                val nickname: String = "",
                val admin: Boolean = false,
                val id: Int = 0,
                val type: Int = 0,
                val email: String = "",
                val token: String = "",
                val username: String = "")