package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


suspend fun main() {
    coroutineScope {
        println("start...")
        launch {
            withTimeout(5000L) {
                println("hahaha")
                "aaaa"
            }
           // println("s = $s")
        }

        println("end")
        delay(10 * 1000L)
    }
}