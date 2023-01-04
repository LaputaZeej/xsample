package com.laputa.zeej

import com.laputa.zeej.std_0010_kotlin.copyIn
import com.laputa.zeej.std_0010_kotlin.copyOut
import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println("---------")
        val src: Array<Number> = arrayOf(1, 2, 3, 45)
        val dest = Array<Number>(src.size) {
            0
        }
        println(dest.joinToString(","))
//        copyIn(dest = dest,src = src)
        copyOut(dest = dest, src = src)
//        copyOut(dest = dest,src = src)
        println(dest.joinToString(","))
    }

    @Test
    fun t11111() = runBlocking {
        println("============================")
        launch {
            delay(500)
            println("launch ${Thread.currentThread()}")
        }

        launch {
            coroutineScope {
                delay(1000)
                println("coroutineScope ${Thread.currentThread()}")
            }
        }


        println("end ${Thread.currentThread()}")
    }

    @Test
    fun t11111222() {
        val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        coroutineScope.launch  {
            println("============================")
            coroutineScope.launch {
                delay(500)
                println("--->launch")
            }

//            coroutineScope {
//                delay(1000)
//                println("--->coroutineScope")
//            }


            println("--->end ")
        }
        println("kkkkkkkkkkkkkkkkkkkk")
        readLine()
    }
}