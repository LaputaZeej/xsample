package com.laputa.zeej.std_0010_kotlin.tailrec

import java.lang.Exception
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

// 尾递归 递归 循环

// 阶乘
// 递归 容易递归太深导致栈溢出
fun factorial(n: UInt): UInt {
    if (n == 0u) return 1u
    return n * factorial(n - 1u)
}

// 循环/迭代
fun factorial01(n: UInt): UInt {
    var result = 1u
    for (i in 1u..n) {
        result *= i
    }
    return result
}

// 尾递归 实现
fun factorial02(n: UInt): UInt {
    tailrec fun factorialInner(n: UInt, result: UInt): UInt {
        if (n == 0u) return result
        return factorialInner(n - 1u, n * result)
    }
    return factorialInner(n, 1u)
}

// 非那波且数列
fun fibonacci(u: UInt): UInt {
    if (u < 2u) return u
    return fibonacci(u - 1u) + fibonacci(u - 2u)
}

fun fibonacci01(u: UInt): UInt {
    var pre = 0u
    var cur = 1u
    for (i in (2u..u)) {
//        val temp = cur
//        cur = pre + cur
//        pre = temp

        //
        cur += pre
        pre = cur - pre
        //
    }
    return cur
}

fun fibonacci02(u: UInt): UInt {
    tailrec fun fibonacciInner(u: UInt, pre: UInt, cur: UInt): UInt {
        if (u < 2u) return cur
        return fibonacciInner(u - 1u, cur, cur + pre)

    }
    return fibonacciInner(u, 0u, 1u)
}

@ExperimentalTime
inline fun timeClock(block: () -> UInt) {
    println(measureTime { block() }.inWholeNanoseconds)
}

@ExperimentalTime
suspend fun main() {
    timeClock {
        factorial(50u)
    }
    timeClock {
        factorial01(50u)
    }
    timeClock {
        factorial02(50u)
    }
//    timeClock {
//        fibonacci01(50u)
//    }
//    timeClock {
//        fibonacci02(50u)
//    }
//
//    try {
//        timeClock {
//            fibonacci(50u)
//        }
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
}