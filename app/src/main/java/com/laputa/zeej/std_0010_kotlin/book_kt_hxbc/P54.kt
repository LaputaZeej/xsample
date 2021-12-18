package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

import kotlin.properties.Delegates

internal var name by Delegates.notNull<Int>()

fun main() {
    name = 1
}