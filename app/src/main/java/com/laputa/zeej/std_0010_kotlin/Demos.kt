package com.laputa.zeej.std_0010_kotlin

import kotlinx.coroutines.*
import java.lang.IllegalStateException

internal fun t1(a: Class<*>) {

}

internal fun t3(a: Class<out Any>) {

}

internal fun <T : Any> t2(a: Class<T>) {

}

internal fun <T : Number?> copyIn(dest: Array<in T>, src: Array<T>) {
    if (dest.size != src.size) throw IllegalStateException()
    src.forEachIndexed { index, number ->
        dest[index] = number
    }
}

internal fun <T : Number?> copyOut(dest: Array<T>, src: Array<out T>) {
    if (dest.size != src.size) throw IllegalStateException()
    src.forEachIndexed { index, number ->
        dest[index] = number
    }
}

internal fun <T : Number?> copyNormal(dest: Array<T>, src: Array<T>) {
    if (dest.size != src.size) throw IllegalStateException()
    src.forEachIndexed { index, number ->
        dest[index] = number
    }
}

fun test1112121() = runBlocking {
    launch (start =CoroutineStart.ATOMIC ){
        delay(10*1000L)
        println("1 ${Thread.currentThread()}")
    }
    launch {
        println("2 ${Thread.currentThread()}")
    }
    launch(start =CoroutineStart.UNDISPATCHED) {
        println("3 ${Thread.currentThread()}")
    }
    launch(start =CoroutineStart.DEFAULT) {
        println("4 ${Thread.currentThread()}")
    }
    println("5 ${Thread.currentThread()}")
}

fun t11111222() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    coroutineScope.launch {


        launch {
            println("1")
        }
        launch {
            println("2")
        }
        launch {
            println("3")
        }



        println("============================")
        coroutineScope.launch {
            delay(2000)
            println("--->launch")
        }

//        launch {
        coroutineScope {
            delay(500)
            println("--->coroutineScope")
        }
//        }


        println("--->end ")
    }
    println("kkkkkkkkkkkkkkkkkkkk")
    readLine()
}

fun main() {
    println("---------")
    test1112121()
//    t11111222()

//
//    val src :Array<Float> = arrayOf(1f,2f,3f,4f)
//    val dest = arrayOfNulls<Number>(src.size)
//    copyIn(dest = dest,src = src)
//    copyOut(dest = dest,src = src)
////    copyNormal(dest = dest,src = src)
//    println(dest.joinToString(","))
}

internal fun test121212121() {


    t1(Int::class.java)
    t2(Int::class.java)
    t3(Int::class.java)
    val a: MutableList<*> = mutableListOf(1, 2)
    val b: MutableList<in Number?> = mutableListOf()
    val aaaa: Any? = null
//    b.add(aaaa)
    val b1: Any? = b[1]
    //val b2 :Number?=  b[1]


}