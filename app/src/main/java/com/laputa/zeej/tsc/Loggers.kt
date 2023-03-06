package com.laputa.zeej.tsc

import android.util.Log

interface Logger {
    fun i(tag: String, msg: String)

    fun e(tag: String, msg: String)

    fun d(tag: String, msg: String)
}

object BLog : Logger {
    // 日志总开关
    var debug = true
    private const val EXT_TAG = "lexy_"

    override fun i(tag: String, msg: String) {
        if (!debug) return
        Log.i("${EXT_TAG}$tag", msg)
    }

    override fun e(tag: String, msg: String) {
        if (!debug) return
        Log.e("${EXT_TAG}$tag", msg)
    }

    override fun d(tag: String, msg: String) {
        if (!debug) return
        Log.d("${EXT_TAG}$tag", msg)
    }
}

