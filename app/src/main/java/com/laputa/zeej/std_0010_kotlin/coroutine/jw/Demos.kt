package com.laputa.zeej.std_0010_kotlin.coroutine.jw

import kotlinx.coroutines.*

fun main() {
    val coroutineScope = CoroutineScope(Dispatchers.Default+CoroutineName("out")+ SupervisorJob())

    val job = coroutineScope.launch {
        launch(CoroutineName("in")+Job()) {
            while (isActive){
                delay(1000)
                println("in is printing")
            }
        }
    }

    coroutineScope.launch {
        delay(2000)
        println("cancel out")
        // 取消外部任务，如果还一直打印，说明不符合结构化并发，即in和out没有联系
        job.cancel()
//        coroutineScope.cancel()
    }
    readLine()
}