package com.yx.framework.di

import com.yx.framework.mvvm.model.api.UserService
import com.yx.framework.net.RetrofitManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent

/**
 * Created by yangxiong on 2020/6/15.
 */
@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {


    @Provides
    fun privderUserServiceImpl(): UserService {
        return RetrofitManager.instance.retrofit.create(UserService::class.java)
    }
}