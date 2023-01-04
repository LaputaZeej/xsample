package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.*
import java.lang.Exception
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private val pools = Executors.newFixedThreadPool(4)

interface Callback<T> {
    fun onSuccess(data: T)

    fun onFail(e: Throwable)
}

fun loadUser(user: String, callback: Callback<String>) {
    pools.execute {
        try {
            println(" ${Thread.currentThread().name} working.......")
            Thread.sleep(1000L)
            callback.onSuccess("$user-${System.currentTimeMillis()}")
        } catch (e: Throwable) {
            callback.onFail(e)
        }
    }
}

fun refreshUi(info: String) {
    println(" ${Thread.currentThread().name} ：info= $info")
}

fun case01() {
    val user = "xpl"
    println(" ${Thread.currentThread().name} case01 开始")
    loadUser(user, object : Callback<String> {
        override fun onSuccess(data: String) {
            // 切换到住线程
            refreshUi(data)
        }

        override fun onFail(e: Throwable) {
            // 切换到住线程
            refreshUi(e.localizedMessage ?: "")
        }
    })
}

suspend fun loadUserSuspend(user: String): String {
    return suspendCancellableCoroutine {
        loadUser(user, object : Callback<String> {
            override fun onSuccess(data: String) {
                it.resume(data)
            }

            override fun onFail(e: Throwable) {
                it.resumeWithException(e)
            }
        })
        it.invokeOnCancellation {

        }
    }
}

fun case02() {
    val coroutineScope = CoroutineScope(CoroutineName("zzzzz"))
    val job = coroutineScope.launch {
        val user = "kt"
        println(" ${Thread.currentThread().name} case02 开始")
        val info = withContext(Dispatchers.IO) {
            loadUserSuspend(user)
        }
        refreshUi(info)
    }
    job.invokeOnCompletion {
        println(" ${Thread.currentThread().name} case02结束！")
    }
}


fun main() {
    println("start")
    runBlocking {
        val scope = CoroutineScope(SupervisorJob())
        scope.launch {
            delay(1000)
            throw Exception()
        }
        scope.launch {
            delay(2000)
            println("job2")
        }
        delay(50000)
        println("end runBlocking")
    }
    println("end main")
//    case01()
//    case02()
//    println(" ${Thread.currentThread().name} end")
//    val coroutineScope = CoroutineScope(CoroutineName("ccccc"))
//    coroutineScope.launch {
//        var index = 0
//        while (true) {
//            delay(1000)
//            println("index=${index++}")
//        }
//
//    }
}