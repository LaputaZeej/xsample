package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

fun ifStatement(flag: Boolean) {
    var a: String? = null
    if (flag) {
        a = "kotlin"
    }
    println(a!!.length)
}

fun ifExpression(flag: Boolean) {
    val a = if (flag) "kotlin" else null
    println(a?.length)
}

fun main() {
    Bird("1")
    Bird(11)
}

internal class Bird constructor(name: String) {

    constructor(age: Int) : this("$age")

    val sex: String by lazy {
        name
    }
//    lateinit var a:Int?=null

    init {
        val wrapper = name
        println(wrapper)
    }

    fun test() {
        val lazy = lazy {
            ""
        }
        lazy.isInitialized()
        lazy.value
    }
}