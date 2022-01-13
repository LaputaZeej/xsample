package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.*
import java.lang.IllegalStateException
import java.lang.RuntimeException

/**
协程取消
 */
private fun logger(msg: String?) {
    println(msg ?: "-")
}

fun main() {
//    cancel001()
//    cancel002()
//    cancel003()
//    cancel004()
//    cancel005()
//    cancel006()
//    cancel007()
    cancel008()
}

internal fun cancel001() {
    val coroutineScopeX = CoroutineScope(Dispatchers.IO + Job() + CoroutineName("x"))

    coroutineScopeX.launch {
        delay(100)
        logger("cancel001-01")
        throw IllegalStateException("error")
    }

    coroutineScopeX.launch {
        delay(200)
        logger("cancel001-02")
    }

    coroutineScopeX.launch {
        delay(300)
        logger("cancel001-03")
    }
    readLine()
}

internal fun cancel002() {
    val coroutineScopeX = CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineName("x"))

    coroutineScopeX.launch {
        delay(100)
        logger("cancel002-01")
        throw IllegalStateException("error")
    }

    coroutineScopeX.launch {
        delay(200)
        logger("cancel002-02")
    }

    coroutineScopeX.launch {
        delay(300)
        logger("cancel002-03")
    }
    readLine()
}

internal fun cancel003() {
    val parentExceptionHandler = CoroutineExceptionHandler { _, e ->
        logger("parentExceptionHandler - ${e.localizedMessage}")
    }

    val childExceptionHandler = CoroutineExceptionHandler { _, e ->
        logger("childExceptionHandler - ${e.localizedMessage}")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO + CoroutineName("cancel003"))
    coroutineScope.launch(parentExceptionHandler) {
        launch(Job(this.coroutineContext[Job]) + childExceptionHandler) {
            delay(200)
            logger(" do something ... ")
            throw IllegalStateException("something error")
        }
    }
    readLine()
}

internal fun cancel004() {
    logger("---------------------------------")
    val parentExceptionHandler = CoroutineExceptionHandler { _, e ->
        logger("parentExceptionHandler - ${e.localizedMessage}")
    }

    val childExceptionHandler = CoroutineExceptionHandler { _, e ->
        logger("childExceptionHandler - ${e.localizedMessage}")
    }
    val coroutineScope = CoroutineScope(Dispatchers.IO + CoroutineName("cancel004"))
    coroutineScope.launch(parentExceptionHandler) {
        launch(SupervisorJob(this.coroutineContext[Job]) + childExceptionHandler) {
            delay(200)
            logger(" do something ... ")
            throw IllegalStateException("something error")
        }
    }
    readLine()
}


// Kotlin协程是如何建立结构化并发的？
// https://mp.weixin.qq.com/s?__biz=MzA5NzA3Njc4NA==&mid=2247488129&idx=1&sn=27f1bc46af365c56084f063ac672e479&chksm=90a701dea7d088c8d1ae4832e0a4cd8b376a2a23e40fb879e4c5a03cd2ed7999c8b96b76eb72&scene=21#wechat_redirect

internal fun cancel005() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + CoroutineName("cancel005"))
    coroutineScope.launch {
        val job01 = launch {
            delay(2000)
            logger("job 01 delay 2000")
        }
        launch {
            delay(1000)
            logger("job 02 delay 1000")
            job01.cancel()
        }
    }
    readLine()
}

internal fun cancel006() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + CoroutineName("cancel006"))
    coroutineScope.launch {
        val job01 = launch {
            Thread.sleep(2000)
            // yield()
            // ensureActive()
            // throw CancellationException()
            // throw RuntimeException("error")
            logger("job 01 sleep 2000")
        }
        launch {
            delay(1000)
            logger("job 02 delay 1000")
            job01.cancel()
        }
    }
    readLine()
}

// CancellationException是一种特殊的Exception，它和Exception的区别在于，如果在协程中抛出CancellationException，除了子协程外并不会影响其它协程
internal fun cancel007() {
    val exceptionHandler = CoroutineExceptionHandler { _, e ->
        logger("exceptionHandler - ${e.localizedMessage}")
    }
    val coroutineScope =
        CoroutineScope(Dispatchers.IO + CoroutineName("cancel007")) + exceptionHandler
    coroutineScope.launch {
        launch {
            logger("job 01")
            delay(2000)
            logger("job 01 end")
        }

        launch {
            logger("job 02")
            launch {
                logger("job 02-01")
                delay(2000)
                logger("job 02-01 end")
            }
            delay(1000)
            //throw CancellationException("job 02 error")
            throw RuntimeException("job 02 error")
            logger("job 02 end")
        }

        launch {
            logger("job 03")
            delay(2000)
            logger("job 03 end")
        }
    }
    readLine()
}

internal fun cancel008() {
    val coroutineScope = CoroutineScope(Dispatchers.IO + CoroutineName("cancel008"))
    coroutineScope.launch {
        val job01 = launch {
            logger("job 01")
            delay(2000)
            logger("job 01 sleep 2000")
        }

        launch {
            delay(1000)
            job01.cancel()
        }

        job01.invokeOnCompletion {
            logger("job 01 invokeOnCompletion")
        }
    }
    readLine()
}

