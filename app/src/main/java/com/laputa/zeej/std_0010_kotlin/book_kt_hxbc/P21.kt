package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

import kotlin.jvm.functions.FunctionN

internal class Book(val name: String) {
    fun show() {
        println("name=$name")
    }
}

fun main() {
    // (String)->Book
    val createBook = ::Book
    val book = createBook("Kotlin核心编程")
    println("createBook = $createBook")
    println("book = $book")
    val show = book::show
    show()
    // (Book)->name
    val nameFun = Book::name
    val name = nameFun(Book("深入理解Kotlin协程"))
    println("Book::name = $name")
    // use
    (1..10)
        .map {
            "book$it"
        }
        .map(createBook)
        .map(nameFun)
        .forEach(::println)
}