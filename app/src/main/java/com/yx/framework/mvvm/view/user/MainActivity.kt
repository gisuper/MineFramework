package com.yx.framework.mvvm.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yx.framework.R
import com.yx.framework.ext.logD
import com.yx.framework.ext.startActivity
import com.yx.framework.mvvm.viewmodel.LoginViewModel
import com.yx.framework.mvvm.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel by viewModels<MainViewModel>()

        coin.setOnClickListener {
            viewModel.coin()
        }

        viewModel.coinBean.observe(this, Observer {
//            logD(it.data.toString())
        })
        logout.setOnClickListener {
            viewModel.logout()
        }
    }
}