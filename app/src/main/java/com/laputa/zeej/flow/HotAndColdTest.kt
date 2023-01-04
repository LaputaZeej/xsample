package com.laputa.zeej.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    println("start")
    debug()
//    test00002()
//    test00003()
    println("end")
    readLine()
}

object DataSource {
    fun createData(print: Boolean = true) = callbackFlow<String> {
        println("=============createData===========")
        var index = 0
        while (this.isActive) {
            delay(1000)
            index++
            if (index > 10) break

            if (print) println(">>> ${this.hashCode()} $index")
            this.trySendBlocking("$index")
        }
        println("createData end")
        close() // 注意这个区别，不结束则会让collect处一直挂起。
        awaitClose {
            println("awaitClose")
        }
    }

    fun createSimple() = flow<String> {
        println("=============createSimple===========")
        var index = 0
        while (index <= 10) {
            delay(1000)
            index++
            this.emit("$index")
        }
    }
}

/*
 * 冷流 是按需创建的，并且会在它们被观察时发送数据；
 * 热流 则总是活跃，无论是否被观察，它们都能发送数据。
 */
private fun test00001() {
    println("---------test00001------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    val flow: Flow<String> = DataSource.createData()
    val job = coroutineScope.launch {
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }
    coroutineScope.launch {
        delay(3000)
        println("关闭collect")
        job.cancel()
    }
}

private fun test00002() {
    println("---------test00002------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    val flow: Flow<String> = DataSource.createData()
    val job = coroutineScope.launch {
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }
    coroutineScope.launch {
        delay(3000)
        println("关闭collect")
        job.cancel()
    }
    // 不同点 DataSource又开始了新的计时
    // 每个新的收集者都会触发数据流的生产者代码块
    coroutineScope.launch {
        delay(5000)
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }

}

private fun test00003() {
    println("---------test00003------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    // 不同点 热流
    // 热流在collect取消时，DataSource任务不会停止。
    // 注意SharingStarted = SharingStarted.Eagerly
    val flow: Flow<String> = DataSource.createData()
        .shareIn(coroutineScope, SharingStarted.Eagerly)
    val job = coroutineScope.launch {
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }
    coroutineScope.launch {
        delay(3000)
        println("关闭collect")
        job.cancel()
    }
}

private fun test00004() {
    println("---------test00004------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    // 不同点 热流
    // 热流在collect取消时，DataSource任务不会停止。
    // 注意SharingStarted = SharingStarted.Eagerly
    val flow: Flow<String> = DataSource.createData()
        .shareIn(coroutineScope, SharingStarted.Eagerly)
    val job = coroutineScope.launch {
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }
    coroutineScope.launch {
        delay(3000)
        println("关闭collect")
        job.cancel()
    }

    // 不同点 开始新的collect 不会重启
    coroutineScope.launch {
        delay(5000)
        println("开始新的collect")
        flow.collect {
            println("<<< collect new :$it")
        }
        println("<<< collect new end") // once
    }
}

private fun test00005() {
    println("---------test00005------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    // 不同点 SharingStarted.WhileSubscribed
    // 没有观察者时，任务重新开始。
    val flow: Flow<String> = DataSource.createData()
        .shareIn(coroutineScope, SharingStarted.WhileSubscribed())
    val job = coroutineScope.launch {
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }
    coroutineScope.launch {
        delay(3000)
        println("关闭collect")
        job.cancel()
    }

    // 不同点 开始新的collect 不会重启
    coroutineScope.launch {
        delay(5000)
        println("开始新的collect")
        flow.collect {
            println("<<< collect new :$it")
        }
        println("<<< collect new end") // once
    }
}

private fun test00006() {
    println("---------test00006------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    // 不同点 SharingStarted.WhileSubscribed（5000）
    // 5秒内，没有观察者时，任务重新开始。
    val flow: Flow<String> = DataSource.createData()
        .shareIn(coroutineScope, SharingStarted.WhileSubscribed(5000))
    val job = coroutineScope.launch {
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }
    coroutineScope.launch {
        delay(3000)
        println("关闭collect")
        job.cancel()
    }

    // 不同点 开始新的collect 不会重启
    coroutineScope.launch {
        delay(5000)
        println("开始新的collect")
        flow.collect {
            println("<<< collect new :$it")
        }
        println("<<< collect new end") // once
    }
}

private fun test00007() {
    println("---------test00007------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    // 不同点 SharingStarted.WhileSubscribed（5000,2）
    // 2 代表 缓存2个没有收到的数据。
    // 本来<<< collect new :5
    // 有了缓存就有了<< collect new :3
    // 有了缓存就有了<< collect new :4
    // 代表缓存了2个数据
    val flow: Flow<String> = DataSource.createData()
        .shareIn(coroutineScope, SharingStarted.WhileSubscribed(5000), 2)
    val job = coroutineScope.launch {
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }
    coroutineScope.launch {
        delay(3000)
        println("关闭collect")
        job.cancel()
    }

    // 不同点 开始新的collect 不会重启
    coroutineScope.launch {
        delay(5000)
        println("开始新的collect")
        flow.collect {
            println("<<< collect new :$it")
        }
        println("<<< collect new end") // once
    }
}

private fun test00008() {
    println("---------test00008------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    // 不同点 SharingStarted.WhileSubscribed（5000,3）,2
    // 测试 3 比 2 优先级告
    val flow: Flow<String> = DataSource.createData()
        .shareIn(coroutineScope, SharingStarted.WhileSubscribed(5000, 2), 3)
    val job = coroutineScope.launch {
        flow.collect {
            println("<<< collect :$it")
        }
        println("<<< collect end") // once
    }
    coroutineScope.launch {
        delay(3000)
        println("关闭collect")
        job.cancel()
    }

    // 不同点 开始新的collect 不会重启
    coroutineScope.launch {
        delay(5000)
        println("开始新的collect")
        flow.collect {
            println("<<< collect new :$it")
        }
        println("<<< collect new end") // once
    }
}

private fun test00009() {
    println("---------test00009------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    val flow: Flow<String> = DataSource.createData(false)
    coroutineScope.launch {
        flow.onCompletion {
            println("${this.hashCode()}-${flow.hashCode()}")
        }.collect {
            println("<<< collect 0 :$it")
        }
        println("<<< collect 0 end")
    }
    coroutineScope.launch {
        delay(3000)
        flow.onCompletion {
            println("${this.hashCode()}-${flow.hashCode()}")
        }.collect {
            println("<<< collect 1 :$it")
        }
        println("<<< collect 1 end")
    }
}


private fun test00010() {
    // 每次都创建了一个单独的Flow 而且由于时是冷流 任务结束后 会走到collect end
    // 对比热流就不行，会一直挂起。
    println("---------test00010------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    val flow: Flow<String> = DataSource.createData(false)
        .shareIn(coroutineScope, SharingStarted.Eagerly)
    coroutineScope.launch {
        flow.collect {
            println("<<< collect 0 :$it")
        }
        println("<<< collect 0 end") // once
    }
    coroutineScope.launch {
        delay(3000)
        flow.collect {
            println("<<< collect 1 :$it")
        }
        println("<<< collect 1 end") // once
    }

//    coroutineScope.launch {
//        delay(5000)
//        flow.collect {
//            println("<<< collect 2 :$it")
//        }
//        println("<<< collect 2 end") // once
//    }
}

private fun test00011() {
    // 试试state
    println("---------test00011------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    val flow: Flow<String> = DataSource.createData(false)
        .stateIn(coroutineScope, SharingStarted.Eagerly, "99")
    coroutineScope.launch {
        flow.collect {
            println("<<< collect 0 :$it")
        }
        println("<<< collect 0 end") // once
    }
    coroutineScope.launch {
        delay(3000)
        flow.collect {
            println("<<< collect 1 :$it")
        }
        println("<<< collect 1 end") // once
    }
}

private fun test00012() {
    println("---------test00012------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    val flow: Flow<String> = DataSource.createSimple()
    coroutineScope.launch {
        flow.onCompletion {
            println("${this.hashCode()}-${flow.hashCode()}")
        }.collect {
            println("<<< collect 0 :$it")
        }
        println("<<< collect 0 end")
    }
    coroutineScope.launch {
        delay(3000)
        flow.onCompletion {
            println("${this.hashCode()}-${flow.hashCode()}")
        }.collect {
            println("<<< collect 1 :$it")
        }
        println("<<< collect 1 end")
    }
}

private fun test00013() {
    println("---------test00013------------")
    val coroutineScope = CoroutineScope(SupervisorJob())

    // 连续设置不会丢失
    val flow: MutableSharedFlow<String> = MutableSharedFlow()
    val flow3 = flow.stateIn(coroutineScope, SharingStarted.Eagerly, "")
    // 等于MutableStateFlow
    val flow2: MutableSharedFlow<String> = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
    // 连续设置值会丢失
    val flow1: MutableStateFlow<String> = MutableStateFlow("1")
    coroutineScope.launch {
        delay(1000)
        flow.emit("1")
        flow.emit("1_1")
        delay(2000)
        flow.emit("2")
        delay(3000)
        flow.emit("3")
    }
    coroutineScope.launch {
        flow.onCompletion {
            println("${this.hashCode()}-${flow.hashCode()}")
        }.collect {
            println("<<< collect 0 :$it")
        }
        println("<<< collect 0 end")
    }
    coroutineScope.launch {
        delay(3000)
        flow.onCompletion {
            println("${this.hashCode()}-${flow.hashCode()}")
        }.collect {
            println("<<< collect 1 :$it")
        }
        println("<<< collect 1 end")
    }
}


private fun test00014() {
    println("---------test000014------------")
    val coroutineScope = CoroutineScope(SupervisorJob())
    // 不同点 SharingStarted.WhileSubscribed
    // 没有观察者时，任务重新开始。
    val flow: Flow<String> = DataSource.createData()
        .shareIn(coroutineScope, SharingStarted.WhileSubscribed())
    coroutineScope.launch {
        flow.collect {
            println("<<< collect 01 :$it")
        }
        println("<<< collect 01 end") // once
    }

    coroutineScope.launch {
        flow.collect {
            println("<<< collect 02 :$it")
        }
        println("<<< collect 02 end") // once
    }
}

private fun debug() {
    val coroutineScope = CoroutineScope(SupervisorJob())
    val flow = flow<Int> {
        var i = 0
        while (true) {
            delay(1000)
            i++
            emit(i)
            println("send $i")
        }
    }
//    val flow = callbackFlow<Int> {
//        var i = 0
//        while (isActive) {
//            delay(1000)
//            i++
//            send(i)
//            println("send $i")
//        }
//        awaitClose {
//            println("awaitClose")
//        }
//    }

//        .shareIn(coroutineScope, SharingStarted.Eagerly)

    val job1 = coroutineScope.launch {
        flow.collect {
            println("collect 01 ->$it")
        }
    }

    val job2 = coroutineScope.launch {
        delay(5000)
        flow.collect {
            println("collect 02 ->$it")
        }
    }

    coroutineScope.launch {
        delay(10_000)
        job1.cancel()
        job2.cancel()
    }
}



