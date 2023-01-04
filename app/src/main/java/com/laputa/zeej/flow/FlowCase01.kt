package com.laputa.zeej.flow

import com.laputa.zeej.std_0010_kotlin.function.f
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


fun main() {
    val cor = CoroutineScope(Job())
    cor.launch {
        foo(1).collect {
            print("collect = $it")
        }
    }
    readLine()
}

private fun download(url:String){
    println("download...")
}

fun foo(n: Int) = flow<Int> {
    try {
        download("")
        this.emit(n + 1)
        //foo(n + 1)
    } catch (e: Throwable) {
        println("error")
    }
}
