package com.yx.framework.mvvm.view.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yx.framework.R
import com.yx.framework.databinding.ActivityRegisterBinding
import com.yx.framework.ext.toast
import com.yx.framework.mvvm.viewmodel.RegisterModel

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityRegisterBinding>(
                this,
                R.layout.activity_register
            )
        val model: RegisterModel = ViewModelProvider(this).get(RegisterModel::class.java)
        binding.registerModel = model

        model.userBean.observe(this, Observer {
            if (it.code >= 0) {
                if (it.data.errorCode == 0) {
                    toast("注册成功：${it.data.data.nickname}")
                } else {
                    toast("注册失败：${it.data.errorMsg}")
                }
            } else {
                toast("注册失败：${it.msg}")
            }
        })
        binding.submit.setOnClickListener {
            model.register(
                binding.etAccount.text.toString(),
                binding.etPwd.text.toString(),
                binding.etPwdAgain.text.toString()
            )
        }

    }
}