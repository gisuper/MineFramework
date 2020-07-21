package com.yx.framework.net

import android.util.Log
import com.elvishew.xlog.LogUtils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.yx.framework.common.*
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by yangxiong on 2020/6/9.
 */
class RetrofitManager private constructor() {
    val TAG = "RetrofitManager"
    private val cookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.context))
    }


    companion object {
        val instance: RetrofitManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManager()
        }
    }

    private val logInterceptor = HttpLoggingInterceptor {
        Log.d("RetrofitManager", "${it}")
    }

    init {
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    private val headInterceptor = Interceptor { chain ->
        val request = chain.request()
        var newHttpUrl = request.url().newBuilder().build()
     Log.d(TAG,"chang_url:${request.header("chang_url")}")
        if ("baidu".equals(request.header("chang_url"))) {
            val oldUrl = request.url()
            val newBaseUrl: HttpUrl = HttpUrl.parse("https://www.baidu.com/")!!
            newHttpUrl = oldUrl.newBuilder().scheme(newBaseUrl.scheme())
                .host(newBaseUrl.host())
                .port(newBaseUrl.port())
                .build()
        }

        val proceed = chain.proceed(
            request.newBuilder()
                .url(newHttpUrl)
                .addHeader("Content-Type", "application/json")
                .build()
        )
        proceed
    }


    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .cookieJar(cookieJar)
        .addInterceptor(headInterceptor)
        .addInterceptor(logInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(HOST_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


}