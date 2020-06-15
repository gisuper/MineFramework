package com.yx.framework.mvvm.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.tabs.TabLayout
import com.yx.framework.mvvm.model.api.Response
import com.yx.framework.mvvm.model.bean.UserBean
import com.yx.framework.mvvm.model.repository.UserRepository
import kotlinx.coroutines.*

/**
 * Created by yangxiong on 2020/6/10.
 */
class RegisterModel : ViewModel() {
    val TAG = "RegisterModel"
    val userBean = MutableLiveData<Response<UserBean>>()
    val repository = UserRepository()

    fun register(
        username: String,
        password: String,
        repassword: String
    ) {
        viewModelScope.launch {
            val response = repository.register(username, password, repassword)
            userBean.value = response
        }
    }
}