package com.yx.framework.mvvm.view.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yx.framework.R
import com.yx.framework.ext.logD
import com.yx.framework.ext.startActivity
import com.yx.framework.mvvm.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val usernameString = intent.getStringExtra("username")

        username.setText(usernameString)

        regist.setOnClickListener {
            startActivity(RegisterActivity::class.java)
            finish()
        }
        login.setOnClickListener {
            viewModel.login(username.text.toString(),password.text.toString())
        }

        viewModel.userBean.observe(this, Observer {
            logD(it.data.toString())
            if (it.code == 0 && it.data.errorCode >= 0){
                startActivity(MainActivity::class.java)
                finish()
            }
        })
    }
}