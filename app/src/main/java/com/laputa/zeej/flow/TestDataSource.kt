package com.laputa.zeej.flow

import android.content.Context
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object TestDataSource {
    fun testFlow(context: Context): Flow<String> = callbackFlow {
        val job = launch {
            while (isActive) {
                kotlinx.coroutines.delay(2000)
                offer("Test:${System.currentTimeMillis()}")
            }
        }
        awaitClose {
            i("[TestDataSource]awaitClose!!!")
            job.cancel()
        }
    }
}