package com.laputa.zeej.std_0010_kotlin.coroutine.jw

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private fun logger(msg: String?) {
    println(msg ?: "-")
}

suspend fun main() {
//    coroutine001()
//    coroutine002()
    coroutine003()

}

private suspend fun coroutine001() {
    logger("start")
    suspendCoroutine<Unit> {
        logger("in suspendCoroutine ...")
    }
    logger("end")
}

private suspend fun coroutine002() {
    logger("start")
    suspendCoroutine<Unit> {
        logger("in suspendCoroutine ...")
        it.resume(Unit)
    }
    logger("end")
}


private var mCon :Continuation<String>? = null
private var mCon01 :Continuation<String>? = null
private suspend fun coroutine003(){
    logger("start")
    val result = suspendCoroutine<String> {
        mCon = it
        val temp = ::mCon.get()
        ::mCon.set(null)
        temp?.resume("hello")
    }
    mCon?.resume("hello")
    logger("end $result")

}