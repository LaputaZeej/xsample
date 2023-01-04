package com.laputa.zeej.flow

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import java.lang.IllegalStateException
import kotlin.coroutines.cancellation.CancellationException

class ExceptionTransparencyViolatedDemo {

    // Exception transparency ensures that if a `collect` block or any intermediate operator throws an exception, then no more values will be received by it.
    // 异常透明性确保如果一个“collect”块或任何中间操作符抛出异常，那么它将不再接收任何值。
    suspend fun test() {
        val flow = flow {
            emit(1)
            try {
                emit(2)
            } catch (e: Throwable) {
                println("first e :$e")
            }
            println("--------------")
            try {
                emit(2)
            } catch (e: Throwable) {
                emit(3)
            }
        }

        Job.Key
        flow
            .catch {
                // println("exception :${this.emit(4)}")
                println("catch")
            }
            .onCompletion {
                println("onCompletion")
            }
            .collect { value ->
                if (value == 2) {
                    //throw CancellationException("够了够了！No more elements required, received enough")
                    throw IllegalStateException("够了够了！No more elements required, received enough")
                } else {
                    println("Collected $value")
                }
            }
    }
}

suspend fun main() {

    ExceptionTransparencyViolatedDemo().test()
    println("---")
}