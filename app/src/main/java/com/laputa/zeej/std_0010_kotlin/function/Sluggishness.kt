package com.laputa.zeej.std_0010_kotlin.function

// 惰性求值

fun f1(x: Int, y: Int): Int = x
fun f2(x: Int): Int = f2(x) // 栈溢出
tailrec fun f3(x: Int): Int = f3(x) // 死循环

fun main() {
    // 死循环也是一种相同的结果 （纯函数）
    println(f1(1, f2(2)))
    println(f1(1, f3(2)))
}

fun lazyPrint(msg: String) = {
    print(msg)

}

