//package com.laputa.zeej.std_0010_kotlin.q1
//
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//
//object Demo {
//    private val scope: CoroutineScope =
//        CoroutineScope(Dispatchers.IO + CoroutineName("Demo") + SupervisorJob())
//
//    private val result: MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(mapOf())
//    private var uploadDotListJob: Job? = null
//
//    fun uploadDotList(n: Int) {
//        uploadDotListJob?.cancel()
//        uploadDotListJob = scope.launch {
//            try {
//                (1..n).forEach {
//                    launch {
//                        val id = it.toString()
//                        val r = upload(it.toString())
//                        result.emit()
//                    }
//                }
//            } catch (e: Throwable) {
//
//            } finally {
//            }
//        }
//    }
//
//    private suspend fun upload(id: String): Boolean {
//        delay(10000)
//        return true
//    }
//}