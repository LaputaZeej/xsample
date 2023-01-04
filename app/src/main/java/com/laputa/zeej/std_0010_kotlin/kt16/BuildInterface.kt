package com.laputa.zeej.std_0010_kotlin.kt16

import kotlin.experimental.ExperimentalTypeInference

class Container<T>{
    fun set(value:T){

    }
}

@OptIn(ExperimentalTypeInference::class)
fun <T> buildContainer(@BuilderInference build:Container<T>.()->Unit){

}

fun test(){
    buildContainer{
        set("")
    }
}