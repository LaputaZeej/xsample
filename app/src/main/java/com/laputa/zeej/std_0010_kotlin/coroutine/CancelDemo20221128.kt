package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.*
import java.lang.IllegalStateException

fun main() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    coroutineScope.launch {
        try {
            slow(leastTime=1000) {
                delay(2000)
                throw CancellationException("测试异常")
            }
            println(">>>>>>>>>>> ok <<<<<<<<<<")
        } catch (e: Throwable) {
            e.printStackTrace()
            println("error ${e.message}")
        }

        coroutineScope.launch {
            while (true) {
                delay(2000)
                println("--->")
            }
        }
    }

    println("end.....")
    readLine()

//    coroutineScope.launch {
//        slow(leastTime = 1000L) {
//
//        }
//    }
}




suspend fun slow(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    leastTime: Long,
    job: suspend () -> Unit,
) {
    withContext(dispatcher) {
        listOf(async { job() }, async { delay(leastTime) }).awaitAll()
    }
}

object CancelDemo20221128 {

    fun test(){
        val ch:Byte =1
        if(ch == 0b1111.toByte()){
            println("")
        }
    }
    //

    suspend fun slow(time: Long, block: suspend () -> Unit) {
        coroutineScope {
            launch {
                while (true) {
                    delay(1000)
                    println("slow <----")
                }
            }
            listOf(
                async {
                    block.invoke()
                    println("block invoke")
                },
                async {
                    delay(time)
                    println("time out ")
                }).awaitAll()


        }
    }

}