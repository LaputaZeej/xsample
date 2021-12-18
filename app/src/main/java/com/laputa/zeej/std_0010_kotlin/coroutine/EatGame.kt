package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random
import kotlin.reflect.KMutableProperty0

class EatGame {
    private var feedContinuation: Continuation<Int>? = null
    private var eatContinuation: Continuation<String>? = null
    private var eatAttempts = 0

    var isActive: Boolean = true
        private set

    suspend fun eat(): String {
        return if (isActive) suspendCoroutine {
            this.eatContinuation = it
            resumeContinuation(this::feedContinuation, eatAttempts++)
        } else ""
    }

    suspend fun feed(food: String): Int {
        return if (isActive) suspendCoroutine {
            this.feedContinuation = it
            resumeContinuation(this::eatContinuation, food)
        } else -1
    }

    fun timeout() {
        isActive = false
        resumeContinuation(this::feedContinuation, eatAttempts)
        resumeContinuation(this::eatContinuation, "")
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

        suspend fun testEatGame() {
            coroutineScope {
                val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
                val game = EatGame()
                launch(dispatcher) {
                    println("Ready Go!")
                    delay(60*1000L)
                    game.timeout()
                    println("time out!")
                }
                launch(dispatcher) {
                    while (game.isActive) {
                        delay(1000)
                        val feed = Random.nextInt()
                        println("[${Thread.currentThread().name} #1] Feed = $feed")
                        println("[${Thread.currentThread().name} #1] Feed Complete:${game.feed("$feed")}")
                    }
                }

                launch(dispatcher) {
                    while (game.isActive) {
                        //delay(50)
                        println("[${Thread.currentThread().name} #2] Eat = ${game.eat()}")
                    }
                }
            }
        }
    }
}


suspend fun main() {
    EatGame.testEatGame()
}