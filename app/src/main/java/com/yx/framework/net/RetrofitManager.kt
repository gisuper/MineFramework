package com.yx.framework.net

import android.util.Log
import com.yx.framework.common.CONNECT_TIMEOUT
import com.yx.framework.common.HOST_URL
import com.yx.framework.common.READ_TIMEOUT
import com.yx.framework.common.WRITE_TIMEOUT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by yangxiong on 2020/6/9.
 */
class RetrofitManager private constructor() {

    val TAG = "RetrofitManager"

    companion object {
        val instance: RetrofitManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManager()
        }
    }

    private val logInterceptor = HttpLoggingInterceptor{
        Log.d("RetrofitManager","${it}")
    }

    init {
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    private val headInterceptor = Interceptor { chain ->
        chain.proceed(
            chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }


    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(logInterceptor)
        .addInterceptor(headInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(HOST_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}