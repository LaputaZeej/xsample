package com.laputa.zeej.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

suspend fun a() {
    delay(100)
    Thread.sleep(100) // Inappropriate blocking call
    Thread {}.start()
}

private fun sleepExt(long: Long) {
    Thread.sleep(long)
}

private fun createFlow(tag: String = ""): Flow<Int> = flow<Int> {
    var index = 0
    while (true) {
        delay(1000)
        //sleepExt(1000)
        println("[$tag]working $index ......")
        emit(index++)
    }
}
// 普通flow是把代码块放到了对象里，当collect时才会执行，所以说是冷。也就是说 由 消费者触发生产。
// 而热流 则无论有没有观察者都不影响数据产生。

fun main() {
    println("------")
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("zeej"))
    val coldFlow = createFlow()
    val hotFlow = createFlow("hot").stateIn(coroutineScope, SharingStarted.WhileSubscribed(), 0)
    test(coroutineScope, coldFlow)
    //test(coroutineScope, hotFlow)
    println("wait pressed.")
    readLine()

//    test0001()
}

fun test0001() {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("zeej"))
    val coldFlow = createFlow()/*.stateIn(coroutineScope, SharingStarted.Eagerly, 0)*/
    val job01 = coroutineScope.launch {
        coldFlow.collect {
            println("job 01 -> $it")
        }
    }
    val job02 = coroutineScope.launch {
        coldFlow.collect {
            println("job 02 -> $it")
        }
    }
    coroutineScope.launch {
        delay(5000)
        job01.cancel()
        job02.cancel()
        println("job 01/job 02 cancel")
        val job03 = coroutineScope.launch {
            coldFlow.collect {
                println("job 03 -> $it")
            }
        }
        delay(5000)
        job03.cancel()
        println("job 03 cancel")
    }
    readLine()
}

private fun test(
    coroutineScope: CoroutineScope,
    coldFlow: Flow<Int>
) {
    val job1 = coroutineScope.launch(Dispatchers.IO) {
        createFlow("1").onCompletion {
            println("********job1******** flow over")
        }.collect {
            println("1->$it")
        }
    }
    job1.invokeOnCompletion {
        println("========job1=========over")
    }

    val job2 = coroutineScope.launch(Dispatchers.IO) {
        createFlow("2").onCompletion {
            println("********job2******** flow over")
        }.collect {
            println("2->$it")
        }
    }
    job2.invokeOnCompletion {
        println("========job2=========over")
    }

//    val jobX = coroutineScope.launch(Dispatchers.IO) {
//        coldFlow.collect {
//            println("x->$it")
//        }
//    }

    coroutineScope.launch {
        delay(5000)
        job2.cancel()
        println("job2 cancel")
        delay(3000)
        val job3 = launch {
            createFlow("3").onCompletion {
                println("********job3******** flow over")
            }.collect {
                println("3->$it")
            }
        }
        job3.invokeOnCompletion {
            println("========job3=========over")
        }
        println("job3 start")
        delay(5000)
        job1.cancel()
        job3.cancel()
        println("job1 & job3 cancel")
        val job4 = launch {
            createFlow("4").onCompletion {
                println("********job4******** flow over")
            }.collect {
                println("4->$it")
            }
        }
        job4.invokeOnCompletion {
            println("========job4=========over")
        }
        println("job4 start")
        delay(4000)
        job4.cancel()
        println("job4 cancel")
//        delay(2000)
//        jobX.cancel()
//        coroutineScope.cancel()
//        println("coroutineScope cancel")
    }
}