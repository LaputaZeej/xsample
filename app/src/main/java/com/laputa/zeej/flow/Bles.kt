package com.laputa.zeej.flow

import android.view.View
import androidx.lifecycle.liveData
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.regex.Pattern
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

sealed interface ScanState {
    object Start : ScanState
    object Stop : ScanState
    data class Scanning(val device: BleDevice) : ScanState
    data class Finished(val devices: List<BleDevice>) : ScanState
    data class Error(val error: Throwable) : ScanState
}

fun scanFlow(): Flow<ScanState> = callbackFlow {
    try {
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {
                if (success) {
                    trySendBlocking(ScanState.Start)
                } else {
                    trySendBlocking(ScanState.Stop)
                }
            }

            override fun onScanning(bleDevice: BleDevice) {
                trySendBlocking(ScanState.Scanning(bleDevice))
            }

            override fun onScanFinished(scanResultList: MutableList<BleDevice>) {
                trySendBlocking(ScanState.Finished(scanResultList))
            }

        })
    } catch (e: Throwable) {
        trySendBlocking(ScanState.Error(e))
    }

    awaitClose {
        BleManager.getInstance().cancelScan()
    }
}


interface Callback {
    fun onSuccess(msg: String)
    fun onError(e: Throwable)
}

class Api {
    fun api(callback: Callback) {
        // ...
    }

    fun cancel() {
        // ...
    }
}

suspend fun suspendApi(): String = suspendCancellableCoroutine {
    val api = Api()
    api.api(object : Callback {
        override fun onSuccess(msg: String) {
            it.resume(msg)
        }

        override fun onError(e: Throwable) {
            it.resumeWithException(e)
        }
    })
    it.invokeOnCancellation {
        api.cancel()
    }
}


fun main() {
    val text = "ad131414刘亦菲13141哈哈*（……%sjfjs"
//    val compile = Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$")
    val compile = Pattern.compile("[A-Z]")
    val matcher = compile.matcher(text)
    val sb = StringBuffer().append("r=")
    while (matcher.find()){
        sb.append(matcher.group())
    }
    println(sb.toString())
}

