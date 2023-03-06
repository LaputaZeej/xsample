package com.laputa.zeej.std_0010_kotlin.coroutine.jw

import kotlinx.coroutines.*

// https://mp.weixin.qq.com/s?__biz=MzA5NzA3Njc4NA==&mid=2247487995&idx=2&sn=096c1dc0ee142ff4bbea7537ea8990e1&chksm=90a702a4a7d08bb2922005cfb0495be9838871084eacc4dd16efec5dfebee4f9d239d5b13bfc&cur_album_id=2148038531200417794&scene=189#wechat_redirect

private fun logger(msg: String?) {
    println(msg ?: "-")
}



private fun case01() {
    val outerScope = CoroutineScope(Dispatchers.IO + CoroutineName("case01") + SupervisorJob())
    val job = outerScope.launch(Job()) {
        val job01 = launch() {
            logger("case01 -- job 01 + ${this.coroutineContext[Job]}")
            delay(1000)
            logger("case01 -- job 01 cancel + ${this.coroutineContext[Job]}")
            outerScope.cancel()
        }

        val job02 = launch {
            logger("case01 -- job 02  ${this.coroutineContext[Job]}")
            delay(3000)
            logger("case01 -- job 02 cancel + ${this.coroutineContext[Job]}")
        }

        job01.invokeOnCompletion {
            logger("job01 cancel!!!")
        }

        job02.invokeOnCompletion {
            logger("job02 cancel!!!")
        }


    }
    job.invokeOnCompletion {
        logger("job cancel!!!")
    }

}

internal val viewModelScope =
    CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("case02"))
private var job: Job? = null
private fun case02() {
    job = viewModelScope.launch {
        launch() {
            delay(1000)
            println("case02 -- job 01 cancel")
            job?.cancel()
        }

        launch {
            delay(2000)
            println("case02 -- job 02 cancel")
        }

        viewModelScope.launch(Job() + Dispatchers.IO) {
            delay(3000)
            println("case03 -- job 03 cancel")
        }

        coroutineScope {
            viewModelScope.launch {
                delay(4000)
                println("case04 -- job 04 cancel")
            }
        }
    }
}

private  fun test(){
    viewModelScope.launch {
        delay(2000)
        println("case04 -- job 05 cancel")
    }
}

fun main() {
    case02()
    readLine()
}

