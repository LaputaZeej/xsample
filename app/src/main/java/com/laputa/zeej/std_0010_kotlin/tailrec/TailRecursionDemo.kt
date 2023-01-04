package com.laputa.zeej.std_0010_kotlin.tailrec

import java.math.BigInteger

// 普通递归阶乘
fun factorial(num: Int): BigInteger {
    if (num == 0) return BigInteger.valueOf(1)
    return BigInteger.valueOf(num.toLong()).times(factorial(num - 1))
}

// 不正确的尾递归 Tailrec 编译期无法优化
tailrec fun factorialTailrecWrong(num: Int): BigInteger { // A function is marked as tail-recursive but no tail calls are found
    if (num == 0) return BigInteger.valueOf(1)
    return BigInteger.valueOf(num.toLong()).times(factorialTailrecWrong(num - 1))
}

// 正确的尾递归 Tailrec 编译期优化为for循环
class Result(var value: BigInteger = BigInteger.valueOf(1))

tailrec fun factorialTailrec(num: Int, result: Result) {
    if (num == 0) {
        result.value = result.value.times(BigInteger.valueOf(1))
    } else {
        result.value = result.value.times(BigInteger.valueOf(num.toLong()))
        factorialTailrec(num - 1, result)
    }
}


fun main() {
    println(factorial(5))
    // println(factorial(10000)) // StackOverflowError
    val result = Result()
    factorialTailrec(10000, result)
    println("result  = ${result.value}")

}