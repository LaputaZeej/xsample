package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.lang.IllegalStateException


fun t() {
    println("1 ${Thread.currentThread()}")
    val result = runBlocking(Dispatchers.IO) {
        Thread.sleep(1000)
        println("2 ${Thread.currentThread()}")
        "hello runBlocking"
    }
    println("3 ${Thread.currentThread()} $result")
}

fun t2() {
    println(0)
    val job = GlobalScope.launch {
        delay(1000)
        println('1')
    }
    println(2)
    Thread.sleep(200)
    // job.cancel()
    // GlobalScope.cancel() //error
}

fun t3() {
    println(0)
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    val job = coroutineScope.launch {
        delay(1000)
        println('1')
    }
    println(2)
    Thread.sleep(200)
    job.cancel()
    coroutineScope.cancel()
}

fun testAsync() {
    println(0)
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    coroutineScope.launch {
//        supervisorScope {


        val s = async {
            println(1)
            delay(1000)
            println(2)
            throw IllegalStateException("error") // 异常没有引起程序崩溃
            println(3)
        }
//            try {
        val result = s.await()
        println("result = $result")
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                println("error $e")
//            }
//        }
    }
    println(10000)
    Thread.sleep(2000)
    println(10001)


}

fun testNonCancellable() {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    coroutineScope.launch {
        try {
            println("1 ")
        } catch (e: Throwable) {
            println("2 $e")
        } finally {
            withContext(NonCancellable) {
                delay(3000)
                println("clear ok ")
            }
            delay(2000)
            println("clear ok 2 ")
        }
    }
    Thread.sleep(1000)
    println("3")
    coroutineScope.cancel()
}

fun testwithTimeout() {
    runBlocking {
        println(1)
        val s: String = try {
            withTimeout(5000) {
                delay(6000)
                "time out 5000"
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            "$e"
        }
        println(s)

    }
    println(2)
}

fun testException() = runBlocking {
    /*   val job = GlobalScope.launch { // root coroutine with launch
           println("Throwing exception from launch")
           throw IndexOutOfBoundsException() // Will be printed to the console by Thread.defaultUncaughtExceptionHandler
       }
       job.join()
       println("Joined failed job")*/
    val deferred = GlobalScope.async { // root coroutine with async
        println("Throwing exception from async")
        throw ArithmeticException() // Nothing is printed, relying on user to call await
    }
    try {
        deferred.await()
        println("Unreached")
    } catch (e: ArithmeticException) {
        println("Caught ArithmeticException")
    }
}

fun testException1() {
    val coroutineScope = CoroutineScope(Dispatchers.IO)
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
    coroutineScope.launch {
        val job = GlobalScope.launch(handler) {
            launch { // the first child
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    withContext(NonCancellable) {
                        println("Children are cancelled, but exception is not handled until all children terminate")
                        delay(100)
                        println("The first child finished its non cancellable block")
                    }
                }
            }
            launch { // the second child
                delay(10)
                println("Second child throws an exception")
                throw ArithmeticException()
            }
        }
        job.join()
    }

}

val counterContext = newSingleThreadContext("CounterContext")
fun main() {
//    t()
//    t2()
//    t3()
//    testAsync()
//    testNonCancellable()
//    testwithTimeout()
//    testException()
//    testException1()

    test()
    println("end")


    //Thread.sleep(10000)
}


internal fun test() = runBlocking {
    val channel = Channel<Int>(Channel.UNLIMITED)
    var i = 0
    arrayOf("a", "b", "c").forEach {
        launch {
            while (i < 1000) {
                println("$it->$i")
                channel.send(i++)
                yield()
            }
        }
    }
}


