package com.laputa.zeej.std_0010_kotlin


abstract class AbsP{
    protected abstract fun pick()
}

class P1:AbsP(){
    public override fun pick() {

    }

}

fun main() {
    val a :AbsP = P1()
    val b :P1 = P1()
    val b1 = P1()
    b.pick()
    b1.pick()

}