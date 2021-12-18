package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

private class Ca(val name: String, val age: Int) {
    init {
        println("init:Ca")
    }

    companion object {
        init {
            println("init:companion object")
        }

        fun newInstance(name: String) = Ca(name, 1)
    }
}

fun main() {
    val ca = Ca.newInstance("kt")
}