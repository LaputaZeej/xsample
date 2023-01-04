package com.laputa.zeej

import kotlinx.coroutines.*
import java.lang.IllegalStateException

class TestSupervisorJob {
    private val exceptionDispatcher = CoroutineExceptionHandler() { _, _ ->
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main


    )
    private val scope2 = CoroutineScope(SupervisorJob() + Dispatchers.Main+exceptionDispatcher


    )


//    fun case02(lifScope: CoroutineScope) {
//
//        val job = lifScope.launch {
//            val job1 = scope2.launch() {
//                delay(1000)
////                try {
//                    throw IllegalStateException()
////                }catch (e:Throwable){
////                    e.printStackTrace()
////                }
//
//            }
//            val job2= launch {
//                delay(2000)
//                println("xxxx job2")
//            }
//            job1.invokeOnCompletion {
//                println("xxxx job1 cancel $it")
//            }
//            job2.invokeOnCompletion {
//                println("xxxx job2 cancel $it")
//            }
//            delay(5000)
//            println("xxxx end runBlocking")
//        }
//        job.invokeOnCompletion {
//            println("xxxx job cancel $it")
//        }
//    }

    fun case01() {

        val job = scope.launch {
            val job1 = scope.launch() {
                    delay(1000)
//                try {
                    throw IllegalStateException()
//                }catch (e:Throwable){
//                    e.printStackTrace()
//                }
            }
            val job2= launch {
                delay(2000)
                println("xxxx job2")
            }
            job1.invokeOnCompletion {
                println("xxxx job1 cancel $it")
            }
            job2.invokeOnCompletion {
                println("xxxx job2 cancel $it")
            }
            delay(5000)
            println("xxxx end runBlocking")
        }
        job.invokeOnCompletion {
            println("xxxx job cancel $it")
        }
    }


}