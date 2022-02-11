package com.laputa.zeej.std_0010_kotlin.coroutine.jw

import kotlinx.coroutines.*
import java.lang.IllegalStateException


/**
 * 协程异常处理机制
 * 参考：https://mp.weixin.qq.com/s/ZxZs9n85i-PMVoqB3LnhxA
 *
 */

private fun logger(msg: String?) {
    println(msg ?: "-")
}

private fun Throwable.display(tag: String) {
    logger("[$tag]-${this.localizedMessage}")
}

private fun tryDoSomething(tag: String) {
    try {
        throw IllegalStateException("tryDoSomething error!")
    } catch (e: Throwable) {
        // e.printStackTrace()
        e.display(tag)
    }
}

fun main() {
//    exception001()
//    exception002()
//    exception003()
//    exception004()
//    exception005()
//    exception006()
//    exception00601()
    exception00602()
//    exception007()
//    exception008()
//    exception009()
//    exception010()
}


internal fun exception001() {
    tryDoSomething("exception001")
}

internal fun exception002() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    coroutineScope.launch {
        tryDoSomething("exception002")
    }
    readLine()
}

private fun thirdApi(coroutineScope: CoroutineScope) {
    logger("coroutineScope ${coroutineScope.coroutineContext[CoroutineExceptionHandler]}]")
    val job = coroutineScope.launch {
        delay(1000)
        throw IllegalStateException("thirdApi error!")
    }
    job.invokeOnCompletion {
        logger("thirdApi cancel $it")
    }
}

internal fun exception003() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    coroutineScope.launch {
        thirdApi(this)
    }
    readLine()
}

internal fun exception004() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    coroutineScope.launch {
        try {
            thirdApi(this)
        } catch (e: Throwable) {
            e.display("exception004")
        }
    }
    readLine()
}

internal fun exception005() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception005")
    }
    val coroutineExceptionHandler002 = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception005-002")
    }
    val coroutineScope =
        CoroutineScope(Dispatchers.IO + Job()) + coroutineExceptionHandler + coroutineExceptionHandler002

    coroutineScope.launch {
        thirdApi(this)

    }
    readLine()
}

internal fun exception006() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception006")
    }
    logger("coroutineExceptionHandler = $coroutineExceptionHandler")
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    val job = coroutineScope.launch {
        thirdApi(this + coroutineExceptionHandler) // 新的job
    }
    job.invokeOnCompletion {
        logger("outer cancel = $it")
    }

    logger("end...")
    readLine()
}

// 。。。
internal fun exception00601() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("coroutineExceptionHandler=$throwable")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    val job = coroutineScope.launch {
        val innerCoroutine =
            CoroutineScope(Dispatchers.IO + Job() + coroutineExceptionHandler) // 注意新的Job 不构成父子关系
        val innerJob = innerCoroutine.async(coroutineExceptionHandler) { // 注意async和launch
            delay(1000)
            throw IllegalStateException("test error !")
        }

        innerJob.invokeOnCompletion {
            println("inner job is cancel!")
        }
        innerJob.await() // 注意这个


    }
//    coroutineScope.launch {
//        delay(100)
//        job.cancel()
//    }
    job.invokeOnCompletion {
        println("outer job is cancel!")
    }
    readLine()
}

internal fun exception00602() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    val outerJob = coroutineScope.launch {
        val innerJob = launch(Job()) {
            delay(2000)
            println("inner job done!")
        }
        innerJob.invokeOnCompletion {
            println("inner job is cancel!")
        }
    }
    coroutineScope.launch {
        delay(1000)
        outerJob.cancel()
    }
    outerJob.invokeOnCompletion {
        println("outer job is cancel!")
    }
    readLine()
}

internal fun exception007() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception007-coroutineExceptionHandler")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    coroutineScope.launch {
        try {
            coroutineScope {
                thirdApi(this + coroutineExceptionHandler)
            }
        } catch (e: Throwable) {
            e.display("exception007-try-catch")
        }
    }
    readLine()
}

internal fun exception008() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception008-coroutineExceptionHandler")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    coroutineScope.launch {
        try {
            supervisorScope {
                thirdApi(this + coroutineExceptionHandler)
            }
        } catch (e: Throwable) {
            e.display("exception008-try-catch")
        }
    }
    readLine()
}

internal fun exception009() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception009-inner")
    }
    val outerCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception009-outer")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job()) + outerCoroutineExceptionHandler
    coroutineScope.launch {
        thirdApi(this + coroutineExceptionHandler)
    }
    readLine()
}

internal fun exception010() {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception010-inner")
    }
    val outerCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.display("exception010-outer")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO + Job()) + outerCoroutineExceptionHandler
    coroutineScope.launch {
        supervisorScope {
            thirdApi(this + coroutineExceptionHandler)
        }
    }
    readLine()
}

// 无法使用try-catch去捕获launch和async作用域的异常
// 只支持launch()传入，async()传入是无效的；全局异常处理并不能阻止协程取消，只是避免因异常而退出程序。

