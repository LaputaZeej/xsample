package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

// 惰性集合
private val LIST = listOf(1, 2, 3, 4, 5, 6, 7)

private fun t1() {
    println(LIST.filter {
        println("filter:$it")
        it > 2
    }.map {
        println("map:$it")
        it * 33
    })
    println(LIST.asSequence().filter {
        println("Sequence filter:$it")
        it > 2
    }.map {
        println("Sequence map:$it")
        it * 33
    }.toList())
}

@ExperimentalTime
private fun t2() {
    val list = (1..10_000_000).toList()
    println(
        measureTime {
            list.filter { it > 2 }.map { it * 33 }
        }
    )

    println(measureTime { list.asSequence().filter { it > 2 }.map { it * 33 }.toList() })
}

private fun t3() {
    val s = LIST.asSequence().filter {
        println("Sequence filter:$it")
        it > 2
    }.map {
        println("Sequence map:$it")
        it * 33
    }
    println(s)
    println(s.toList())
}

private val naturalNumberList = generateSequence(0) { it + 1 }

@ExperimentalTime
fun main() {
//    t1()
//    t2()
//    t3()
    val take = naturalNumberList.take(10)
    println(take.toList())
}
