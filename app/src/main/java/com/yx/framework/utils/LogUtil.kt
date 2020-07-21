package com.shenyu.treasure_kotlin.utils


import android.util.Log
import com.yx.framework.BuildConfig

object LogUtil {
    private val DEBUG = BuildConfig.DEBUG
    private val TAG = "LogUtil"
    fun v(content: String) {
        v(TAG, content)
    }

    fun v(tag: String, content: String?) {
        if (DEBUG) Log.v(tag, content ?: "null")
    }

    fun w(content: String) {
        w(TAG, content)
    }

    fun w(tag: String, content: String?) {
        if (DEBUG) Log.w(tag, content ?: "null")
    }

    fun i(content: String) {
        i(TAG, content)
    }

    fun i(tag: String, content: String?) {
        if (DEBUG) Log.i(tag, content ?: "null")
    }

    fun d(content: String) {
        if (DEBUG) d(TAG, content)
    }

    fun d(tag: String, content: String) {
        if (DEBUG) Log.d(tag, content)
    }

    fun e(content: String?) {
        e(TAG, content ?: "null")
    }

    fun e(tag: String, content: String?) {
        if (DEBUG) Log.e(tag, content ?: "null")
    }
}
