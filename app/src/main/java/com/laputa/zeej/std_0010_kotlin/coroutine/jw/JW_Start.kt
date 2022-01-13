package com.laputa.zeej.std_0010_kotlin.coroutine.jw

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main() {
    start0001()
}

private fun start0001(){
    val coroutineScope = CoroutineScope(Job())
    coroutineScope.launch {
        println("hello coroutine.")
    }
}