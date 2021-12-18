package com.laputa.zeej.other


fun main(args: Array<String>) {
    val b = BBBB()
    println("AAAA.name =" + AAAA.name)
    println("BBBB.name =" + BBBB.name)
    println("b.nameNormal =" + b.nameNormal)

//    BBBB.name = null
    AAAA.name = null

    println("AAAA.name =" + AAAA.name)
    println("BBBB.name =" + BBBB.name)
    println("b.nameNormal =" + b.nameNormal)

}