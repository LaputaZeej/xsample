package com.laputa.zeej.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*

fun main() {
    val coroutineScope = CoroutineScope(SupervisorJob()+Dispatchers.IO)
    println("_______")
    val job = coroutineScope.launch(Dispatchers.IO) {
        t0001()/*.stateIn(this, SharingStarted.Eagerly,0)*/
            .onCompletion {

            }
            .collect {
                println("-->$it")
            }
    }
    coroutineScope.launch(Dispatchers.IO) {
        delay(5000)
        job.cancel()
        println("cancel")
    }
    println("_______z")
    readLine()
}

fun t0001() = flow <Int> {
    var index = 0
    while (true) {
//        ensureActive()
//        delay(1000)
        println("+++")
        Thread.sleep(1000)
        this.emit(index++)
    }
    print("end.............")
//    awaitClose {
//        println("await close")
//    }
//
//    invokeOnClose {
//        println("invokeOnClose")
//    }
}