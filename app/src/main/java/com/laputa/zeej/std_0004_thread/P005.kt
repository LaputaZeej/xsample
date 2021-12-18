package com.laputa.zeej.std_0004_thread

import java.util.concurrent.Executors

class P005 {

    @NotThreadSafe
    class UnsafeSequence {
        private var value = 0
        fun getNext(): Int {
            return value++
        }
    }

}

fun main() {
    val unsafeSequence = P005.UnsafeSequence()
    val pool = Executors.newCachedThreadPool()
    (0..1_000_000).forEach { index ->
        pool.submit {
            unsafeSequence.getNext()
            //println(index)
        }
    }
    Thread.sleep(30_000)
    println("->"+unsafeSequence.getNext())
    readLine()
}