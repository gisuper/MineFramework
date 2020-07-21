package com.yx.framework.mvvm.view.user

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.yx.framework.R
import com.yx.framework.ext.logD
import com.yx.framework.ext.startActivity
import com.yx.framework.ext.toast
import com.yx.framework.mvvm.viewmodel.LoginViewModel
import com.yx.framework.net.ws.WSManager
import com.yx.framework.utils.FileUtil
import com.yx.framework.utils.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initPermissions()

        val viewModel by viewModels<LoginViewModel>()

        val usernameString = intent.getStringExtra("username")

        account.setText(usernameString)

        register.setOnClickListener {
            startActivity(RegisterActivity::class.java)
            finish()
        }
        login.setOnClickListener {
            viewModel.login(account.text.toString(), pwd.text.toString())
        }

        viewModel.userBean.observe(this, Observer {
            logD(it.data.toString())
            if (it.code == 0 && it.data.errorCode >= 0) {
                startActivity(MainActivity::class.java)
                finish()
            } else {
                toast(it.data.errorMsg)
            }
        })
        lifecycleScope.launch(Dispatchers.IO) {
//            WSManager.instance().connect()
        }
    }

    private fun initPermissions() {
        var perms = arrayOf<String?>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        perms = PermissionUtil.hasNotPermissions(this, perms)
        if (perms.size == 0) {
            FileUtil.createLogDir()
            FileUtil.initXLog()
        } else {
            ActivityCompat.requestPermissions(this, perms, 1000)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        if (requestCode == 1000 && PermissionUtil.hasNotPermissions(this, permissions).size == 0) {
            FileUtil.createLogDir()
            FileUtil.initXLog()
        } else {
            finish()
        }
    }




}