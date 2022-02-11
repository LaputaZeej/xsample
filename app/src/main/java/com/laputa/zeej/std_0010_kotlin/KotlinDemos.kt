package com.laputa.zeej.std_0010_kotlin

interface Kitty{
    fun play():Boolean
}

object Black:Kitty{
    override fun play(): Boolean {
        println(" black play")
        return false
    }

    @JvmStatic
    fun run(){
        println("run.")
    }
}

fun main() {
    Black.play()
    Black.run()
}