package com.laputa.zeej.std_0010_kotlin.function

internal fun sum(x: Int, y: Int, z: Int): Int = x + y + z

internal fun curryingSum(x: Int) = { y: Int -> { z: Int -> x + y + z } }

fun main() {
    println(sum(1, 2, 3))
    println(curryingSum(1)(2)(3))
}


