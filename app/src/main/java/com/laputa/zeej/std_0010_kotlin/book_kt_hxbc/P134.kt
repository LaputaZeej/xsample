package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

private interface AA
private interface BB

fun <T> test(t: T) where T : AA, T : BB {
    println(t)
}