package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.*
import java.lang.RuntimeException
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KMutableProperty0

class ThreePrint {
    private var printAContinuation: Continuation<Int>? = null
    private var printBContinuation: Continuation<Int>? = null
    private var printCContinuation: Continuation<Int>? = null
    private var number: Int = 0
    var isActive: Boolean = true
        private set

    suspend fun doPrintA(): Int {
        checkNumber()
        return if (isActive) {
            suspendCoroutine<Int> {
                this.printAContinuation = it
                resumeContinuation(this::printBContinuation, number++)
            }
        } else {
            resumeContinuationError(this::printBContinuation,(RuntimeException("number is $number")))
            number
        }
    }

    suspend fun doPrintB(): Int {
        checkNumber()
        return if (isActive) {
            suspendCoroutine<Int> {
                this.printBContinuation = it
                resumeContinuation(this::printCContinuation, number++)

            }
        } else {
            resumeContinuationError(this::printCContinuation,(RuntimeException("number is $number")))
            number
        }
    }

    suspend fun doPrintC(): Int {
        checkNumber()
        return if (isActive) {
            suspendCoroutine<Int> {
                this.printCContinuation = it
                resumeContinuation(this::printAContinuation, number++)
            }
        } else {
            resumeContinuationError(this::printAContinuation,(RuntimeException("number is $number")))
            number
        }
    }

    private fun checkNumber() {
        isActive = number < 50
    }


    companion object {
        fun <T> resumeContinuation(
            continuationRef: KMutableProperty0<Continuation<T>?>,//属性引用
            value: T
        ) {
            val continuation = continuationRef.get()
            continuationRef.set(null)
            continuation?.resume(value)
        }

        fun resumeContinuationError(
            continuationRef: KMutableProperty0<Continuation<Int>?>,//属性引用
            error: Throwable
        ) {
            val continuation = continuationRef.get()
            continuationRef.set(null)
            continuation?.resumeWithException(error)
        }

        suspend fun testThreePrint() {
            println("start")
            coroutineScope {
                val threePrint = ThreePrint()
                val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
                val delayTime = 100L

                launch(dispatcher + CoroutineName("A")) {
                    while (threePrint.isActive) {
                        delay(delayTime)
                        println("[${Thread.currentThread().name}-${this.coroutineContext[CoroutineName]} #A] start")
                        println("[${Thread.currentThread().name}-${this.coroutineContext[CoroutineName]} #A] complete ${threePrint.doPrintA()}")
                    }
                }
                launch(dispatcher + CoroutineName("B")) {
                    while (threePrint.isActive) {
                        delay(delayTime)
                        println("[${Thread.currentThread().name}-${this.coroutineContext[CoroutineName]} #B] start")
                        println("[${Thread.currentThread().name}-${this.coroutineContext[CoroutineName]} #B] complete ${threePrint.doPrintB()}")
                    }
                }
                launch(dispatcher + CoroutineName("C")) {
                    while (threePrint.isActive) {
                        delay(delayTime)
                        println("[${Thread.currentThread().name}-${this.coroutineContext[CoroutineName]} #C] start")
                        println("[${Thread.currentThread().name}-${this.coroutineContext[CoroutineName]} #C] complete ${threePrint.doPrintC()}")
                    }
                }
            }
            println("end")
        }
    }

}

suspend fun main() {
    ThreePrint.testThreePrint()
}