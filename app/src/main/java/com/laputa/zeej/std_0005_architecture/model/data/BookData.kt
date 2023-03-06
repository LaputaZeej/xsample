package com.laputa.zeej.std_0005_architecture.model.data

data class BookData(val name: String, val author: String)

 fun  Iterable<BookData>.display(): String {
    return this.joinToString("\n") {
        "   " + it.name + "(${it.author})"
    }
}