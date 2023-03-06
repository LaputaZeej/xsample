package com.laputa.zeej.gpio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GpioViewModel : ViewModel() {
    private val _result: MutableStateFlow<String> = MutableStateFlow("")
    val result = _result.asStateFlow()

    fun test() {
        viewModelScope.launch {
            val timeJob = launch {
                delay(1000)
                throw java.lang.IllegalStateException("超时")
            }
            val job1 = async(Dispatchers.IO) {
                delay(1000)
                "1"
            }
            val job2 = async(Dispatchers.IO) {
                delay(2000)
                "2"
            }
            val job3 = async(Dispatchers.IO) {
                delay(3000)
                "3"
            }
            val job4 = async(Dispatchers.IO) {
                delay(4000)
                "4"
            }
            try {
                val result = job1.await() + job2.await() + job3.await() + job4.await()
                _result.value = result
            } catch (e: Throwable) {
                _result.value ="${e.message}"
            } finally {
                timeJob.cancel()
            }


        }
    }
}