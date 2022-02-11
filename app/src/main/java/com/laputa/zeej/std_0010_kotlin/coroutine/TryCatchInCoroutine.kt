package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.*

private suspend fun hardWork() = suspendCancellableCoroutine<Unit> {
    var i = 0
    while (it.isActive) {
        println("hardWork doing...$i")
        i++
        if (i > 10){
            println("hardWork done")
            break
        }
        Thread.sleep(1000)
    }

    it.invokeOnCancellation {
        println("hardWork invokeOnCancellation!")
    }
}

class TextView {
    fun setText(text: String) {

    }
}

fun main() {
    val coroutineScope = CoroutineScope(Job())
    var textView: TextView? = TextView()
    coroutineScope.launch {
        try {
            hardWork()
        } catch (e: Throwable) {
            //if (e is CancellationException) throw e //
            e.printStackTrace()
            textView!!.setText("123")
        }
    }

    // 模拟页面退出
    val coroutineScope2 = CoroutineScope(Job())
    coroutineScope2.launch {
        delay(2000)
        textView = null
        coroutineScope.cancel()
    }

    readLine()
}