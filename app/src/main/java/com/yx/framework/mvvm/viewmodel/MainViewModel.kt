package com.yx.framework.mvvm.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yx.framework.mvvm.model.api.Response
import com.yx.framework.mvvm.model.bean.CoinBean
import com.yx.framework.mvvm.model.repository.UserRepository
import kotlinx.coroutines.launch

/**
 * Created by yangxiong on 2020/6/12.
 */
class MainViewModel @ViewModelInject constructor(val repository: UserRepository,
      @Assisted private val savedState: SavedStateHandle
) : ViewModel() {
    val TAG = "LoginViewModel"
    val coinBean = MutableLiveData<Response<CoinBean>>()

    fun coin() {
        viewModelScope.launch {
            val response = repository.coin()
            coinBean.value = response
        }
    }

    fun logout() {
        viewModelScope.launch {
            val response = repository.logout()
            coinBean.value = response
        }
    }
}