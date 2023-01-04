package com.laputa.zeej

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class MainViewModel :ViewModel(){

    init {

    }
    fun test(){

        viewModelScope.launch {
//            runBlocking {
            launch {
                launch {
                    delay(1000)
                    throw IllegalStateException()
                }
                launch {
                    delay(2000)
                    println("job2")
                }
                delay(5000)
                println("end runBlocking")
            }
        }
        println("end main")
    }
}