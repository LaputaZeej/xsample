package com.laputa.zeej.std_0006_android.compose

import kotlinx.coroutines.delay
import kotlin.random.Random

interface SearchRepository {
    suspend fun loadHistory(msg: String): List<String>

    companion object : SearchRepository {
        override suspend fun loadHistory(msg: String): List<String> {
            delay(1000)
            return (0..Random.nextInt(10)).map {
                "$msg$it"
            }
        }
    }

}