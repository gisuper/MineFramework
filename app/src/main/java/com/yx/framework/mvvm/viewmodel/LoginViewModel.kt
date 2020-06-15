package com.yx.framework.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yx.framework.mvvm.model.api.Response
import com.yx.framework.mvvm.model.bean.UserBean
import com.yx.framework.mvvm.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by yangxiong on 2020/6/12.
 */
class LoginViewModel : ViewModel(){
    val TAG = "LoginViewModel"
    val userBean = MutableLiveData<Response<UserBean>>()
    val repository = UserRepository()

    fun login(
        username: String,
        password: String
    ) {
        viewModelScope.launch {
            val response = repository.login(username, password)
            userBean.value = response
        }
    }
}