package com.yx.framework.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yx.framework.mvvm.model.api.Response
import com.yx.framework.mvvm.model.bean.CoinBean
import com.yx.framework.mvvm.model.bean.UserBean
import com.yx.framework.mvvm.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by yangxiong on 2020/6/12.
 */
class MainViewModel : ViewModel() {
    val TAG = "LoginViewModel"
    val coinBean = MutableLiveData<Response<CoinBean>>()
    val repository = UserRepository()

    fun coin() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = repository.coin()
            GlobalScope.launch(Dispatchers.Main) {
                coinBean.value = response
            }
        }
    }
}