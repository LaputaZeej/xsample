package com.laputa.zeej.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun main() {
    println("------")

    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("zeej"))

    val job = coroutineScope.launch(Dispatchers.IO) {
        testFlow().collect {
            println("result is $it")
        }
    }

    coroutineScope.launch(Dispatchers.IO) {
        delay(3000)
        println("job cancel.")
        job.cancel()
    }


    println("wait pressed.")
    readLine()
}

private fun testFlow() = flow {
    val r = testSuspend02()
    emit(r)
}.onCompletion {
    println("flow completion")
}

private suspend fun testSuspend() = suspendCancellableCoroutine<String> {
    val r = test()
    try {
        it.resume(r)
    }catch (e:Throwable){
        it.resumeWithException(e)
    }
    it.invokeOnCancellation {
        //.
    }
}

private suspend fun testSuspend02() :String{
    var i = 0
    while (i < 1000) {
        i++
        println(" ...working...$i")
        delay(1000)
    }
    println("work end.")
    return "ok"
}

private fun test():String{
    var i = 0
    while (i < 1000) {
        i++
        println(" ...working...$i")
        Thread.sleep(1000)
    }
    println("work end.")
    return "ok"
}
