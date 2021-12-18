package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

import java.lang.RuntimeException
import kotlin.random.Random

private inline fun STOP(): Nothing = throw RuntimeException("Stop!!!!")

private fun t() {
    val nextBoolean = Random.nextBoolean()
    val a: Nothing? = if (nextBoolean) {
        TODO("测试Nothing")
    } else {
        null
    }
    println("a=$a")
    STOP()
}

fun main() {
    t()
}