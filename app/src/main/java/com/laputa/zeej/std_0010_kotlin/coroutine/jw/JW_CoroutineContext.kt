package com.laputa.zeej.std_0010_kotlin.coroutine.jw

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private fun logger(msg: String?) {
    println(msg ?: "-")
}

private class MyCoroutineContext : AbstractCoroutineContextElement(MyCoroutineContext) {
    companion object Key : CoroutineContext.Key<MyCoroutineContext>
}

fun main() {
//    t000001()
    t000002()

}

private fun t000001() {
    val coroutineScope =
        CoroutineScope(Dispatchers.IO + Job() + MyCoroutineContext() + CoroutineName("sss"))
    val myCoroutineContext1 = coroutineScope.coroutineContext[MyCoroutineContext]
    val myCoroutineContext2 = coroutineScope.coroutineContext[MyCoroutineContext.Key]
    val coroutineName = coroutineScope.coroutineContext[CoroutineName]
    logger("myCoroutineContext1=$myCoroutineContext1")
    logger("myCoroutineContext2=$myCoroutineContext2")
    logger("coroutineName=$coroutineName")
}


// 将两个CoroutineContext组合成一个CoroutineContext，如果是两个类型相同的Element会返回一个新的Element。
// 如果是两个不同类型的Element会返回一个CombinedContext。如果是多个不同类型的Element会返回一条CombinedContext链表。
private fun t000002() {

    val case01 = Dispatchers.IO + EmptyCoroutineContext
    logger("case01 = $case01")

    // 相同类型的Element
    val case02 = CoroutineName("name-01") + CoroutineName("name-000002")
    logger("case02 = $case02")
    val c1 = MyCoroutineContext()
    val c2 = MyCoroutineContext()
    val case03 = c1 + c2
    logger("c1 = $c1 , c2 = $c2 , case03 = $case03")

    // 当前CoroutineContext没有ContinuationInterceptor
    val case04 = CoroutineName("name-004") + Job()
    logger("case04 = $case04")

    // 当前CoroutineContext只有ContinuationInterceptor
    val case05 = Dispatchers.IO + Job()
    logger("case05 = $case05")

    // 全都有
    val case06 = case05 +CoroutineName("name-006")
    logger("case06 = $case06")
    val handlerException = CoroutineExceptionHandler { coroutineContext, throwable ->
        logger("handlerException:$throwable")

    }
    val case07 = case06 +handlerException
    logger("case07 = $case07")

    //
    val case0801  = case06+Dispatchers.Default
    val case0802  = Dispatchers.Default+case06
    logger("case0801 = $case0801")
    logger("case0802 = $case0802")

    val case09 = case07.minusKey(CoroutineExceptionHandler)
    logger("case09 = $case09")

}