package com.yx.framework.mvvm.viewmodel

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yx.framework.mvvm.model.api.Response
import com.yx.framework.mvvm.model.bean.UserBean
import com.yx.framework.mvvm.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

/**
 * Created by yangxiong on 2020/6/12.
 */
class LoginViewModel @ViewModelInject constructor(
    val repository: UserRepository,
    @Assisted private val savedState: SavedStateHandle
) : ViewModel() {
    val TAG = "LoginViewModel"
    val userBean = MutableLiveData<Response<UserBean>>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = repository.login(username, password)
            if (response!= null && response.data != null) {
                userBean.value = response
            }
        }
    }
}