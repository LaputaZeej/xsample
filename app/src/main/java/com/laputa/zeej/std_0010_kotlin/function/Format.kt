package com.laputa.zeej.std_0010_kotlin.function

// 纯函数

sealed class Format
data class Print(val text: String) : Format()
object NewLine : Format()


fun unsafeInterpreter(str: List<Format>) {
    str.forEach {
        when (it) {
            NewLine -> println()
            is Print -> print(it.text)
        }
    }
}

fun safeInterpreter(str: List<Format>): String {
    return str.fold("") { full, b ->
        when (b) {
            NewLine -> "$full\n"
            is Print -> "$full${b.text}"
        }
    }
}

fun main() {
    val list = listOf(Print("hello world!!"), NewLine, Print("hello kt"),NewLine)
    unsafeInterpreter(list)
    println("******************")
    print(safeInterpreter(list))
}