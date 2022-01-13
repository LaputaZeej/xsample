package com.laputa.zeej.std_0006_android.binder

import android.util.Log

private const val TAG = "_binder_"
fun i(msg: String) {
    Log.i(TAG, msg)
}

fun e(msg: String) {
    Log.e(TAG, msg)
}

const val REQ_CODE = 1000