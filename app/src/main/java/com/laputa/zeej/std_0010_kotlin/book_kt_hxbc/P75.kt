package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

private data class Bird1(val weight: Double, val age: Int, val color: String){

}

fun componentN() {
    val str = "20.0,1,blue"
    val (weight, age, color) = str.split(",")
    println("weight = $weight")
    println("age = $age")
    println("color = $color")
}

fun main() {
    componentN()
    val b = Bird1(1.0, 1, "red")
    val (weight, age, color) = b
    println("weight = $weight")
    println("age = $age")
    println("color = $color")
    val (x, y) = Pair<Int, String>(1, "2")
    println("x = $x , y = $y")
    val (x1, y1) = 3 to "4"
    println("x1 = $x1 , y1 = $y1")
    val (x2, y2, z2) = Triple(1111, "11112", false).apply {
        println("$first + $second + $third")
    }
    println("x2 = $x2 , y2 = $y2 , z2 = $z2")
}

