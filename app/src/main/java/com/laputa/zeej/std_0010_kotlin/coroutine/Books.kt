package com.laputa.zeej.std_0010_kotlin.coroutine

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlin.coroutines.*

// 《深入理解Kotlin协程》
suspend fun main() {
    println("【start】")
//    t1()
//    bookT2()
    bookT3()
//    bookT4()
//    bookT7()
//    bookT8()
    println("【end】")
    readLine()
}

internal suspend fun t1() {
    delay(1000)
    println("hello coroutines")
}

// 开启一个协程
internal fun bookT2() {
    //3.1.1 F
    val continuation = suspend {
        println("in coroutine")
        delay(1000)
        "Hello Coroutines"
    }.createCoroutine(object : Continuation<String> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<String>) {
            when {
                result.isSuccess -> println("end1 ${result.getOrNull()}")
                result.isFailure -> println("end2 ${result.getOrThrow()}")
            }
        }
    })
    continuation.resume(Unit)
}

// 直接运行协程
internal fun bookT3() {
    // book34
    suspend {
        println("in coroutine")
        delay(1000)
        "Hello Coroutines"
    }.startCoroutine(object : Continuation<String> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<String>) {
            when {
                result.isSuccess -> println("end1 ${result.getOrNull()}")
                result.isFailure -> println("end2 ${result.getOrThrow()}")
            }
        }
    })
}

private fun <T> printlnResult(result: Result<T>) {
    when {
        result.isSuccess -> println("end1 ${result.getOrNull()}")
        result.isFailure -> println("end2 ${result.getOrThrow()}")
    }
}

// 使用带receiver的协程体
internal fun bookT4() {
    // 没有直接拥有receiver的lambda写法
    // 封装一个

    fun <R, T> launchCoroutine(receiver: R, block: suspend R.() -> T) {
        block.startCoroutine(receiver, object : Continuation<T> {
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<T>) {
                printlnResult(result)
            }
        })
    }

    // 定义自己的作用域，
    // 可以增加函数 也可以限制
    class ProductScope<T> {
        suspend fun produce(value: T): String {
            println("produce : $value")
            return "自定义作用域里的函数 $value"
        }
    }

    @RestrictsSuspension // 避免危险的调用，见Sequence Builder
    class RestrictsProductScope<T> {
        suspend fun produce(value: T): String {
            println("RestrictsProductScope::produce : $value")
            return "自定义作用域（限制）里的函数 $value"
        }
    }

    launchCoroutine(ProductScope<Int>()) {
        println("use定义自己的作用域")
        produce(123456)
        delay(1000)
        produce(2048) // 这个是返回的值
    }

    launchCoroutine(RestrictsProductScope<Int>()) {
        println("use定义自己的受限制的作用域")
        produce(123456)
        // delay(1000) // error 在这个作用域里，就不能调用外部的挂起函数
        produce(2048) // 这个是返回的值
    }
}

// internal fun runSuspend(block: suspend () -> Unit)
// RunSuspend
// 用于测试和suspend main
internal fun bookT5() {
    // 内部方法/类
}

// 挂起的核心 ： Continuation
internal fun bookT6() {
    // 内部方法/类

    suspend fun nonSuspend() = suspendCoroutine<String> {
        it.resume("我虽然是挂起函数，但我其实没有被真正挂起")
    }

    // 反射调用
    ::nonSuspend.call(object : Continuation<String> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<String>) {
            printlnResult(result)
        }
    })

}

// CoroutineContext
// Element Key
// CombinedContext ：left，element left-biased list

internal class C1(val name: String) : AbstractCoroutineContextElement(C1) {
    companion object Key : CoroutineContext.Key<C1>

    override fun toString(): String = "CoroutineName(${name})"
}

internal class C2(val name: String) : AbstractCoroutineContextElement(C2) {
    companion object Key : CoroutineContext.Key<C2>

    override fun toString(): String = "CoroutineName(${name})"
}

internal class C3(val name: String) : AbstractCoroutineContextElement(C3) {
    companion object Key : CoroutineContext.Key<C3>

    override fun toString(): String = "CoroutineName(${name})"
}

internal class MyInterceptor(
    val name: String,
    override val key: CoroutineContext.Key<*> = ContinuationInterceptor.Key
) :
    ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return continuation
    }

    companion object Key : CoroutineContext.Key<MyInterceptor> // 自己的Key无效？

    override fun toString(): String = "MyInterceptor(${name})"
}

fun bookT7() {
    val interceptor = MyInterceptor("interceptor")
    val context01 = C1("xxxxx")
    val context02 = C2("yyyyy")
    val context03 = C3("zzzzz")
    val context04 = C2("zzzzz")
    val context05 = C2("zzzzz")
    val newContext = interceptor + context01 + context02 + context03
    println(newContext)
    val newContext01 = newContext + context04
    println(newContext01)
    val newContext02 = newContext01 + context05
    println(newContext02)
    val minusKey = newContext02.minusKey(C1.Key)
    println(minusKey)
    val context011 = CoroutineName("xxxxx")
    val context022 = CoroutineName("yyyyy")
    val context033 = CoroutineName("zzzzz")
}

internal class LogInterceptor(override val key: CoroutineContext.Key<*> = ContinuationInterceptor) :
    ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {

        return object : Continuation<T> by continuation {
            override fun resumeWith(result: Result<T>) {
                println(">>>>>>>>>>>>>>>>> a")
                continuation.resumeWith(result)
                println(">>>>>>>>>>>>>>>>> z")
            }

        }
    }

}

// P47
internal fun bookT8() {
    suspend {
        "suspend ......."
    }.startCoroutine(object : Continuation<String> {
        override val context: CoroutineContext
            get() = LogInterceptor()

        override fun resumeWith(result: Result<String>) {
            printlnResult(result)
        }
    })



//    suspend {
//        "suspend ......."
//    }.createCoroutine(object : Continuation<String> {
//        override val context: CoroutineContext
//            get() = LogInterceptor()
//
//        override fun resumeWith(result: Result<String>) {
//            printlnResult(result)
//        }
//    }).resume(Unit)
}





