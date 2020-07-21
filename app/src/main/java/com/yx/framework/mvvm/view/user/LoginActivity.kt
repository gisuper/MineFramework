package com.yx.framework.mvvm.view.user

import android.Manifest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.permissionx.guolindev.PermissionX
import com.shenyu.treasure_kotlin.utils.LogUtil
import com.yx.framework.R
import com.yx.framework.ext.logD
import com.yx.framework.ext.startActivity
import com.yx.framework.ext.toast
import com.yx.framework.mvvm.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            LogUtil.d(it.data.toString())
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

        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE)
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel")
            }
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    toast("All permissions are granted")
                } else {
                    toast("These permissions are denied: $deniedList")
                }
            }
    }



}