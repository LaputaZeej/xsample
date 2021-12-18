package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

// 工厂模式
interface Computer {
    val cpu: String

    companion object{
        fun produce(type: ComputerType) {
            when (type) {
                ComputerType.PC -> PC()
                ComputerType.SERVER -> Server()
            }
        }
    }
}

class PC(override val cpu: String = "Core") : Computer
class Server(override val cpu: String = "Xeon") : Computer
enum class ComputerType {
    PC, SERVER;
}

class ComputerFactory {
    fun produce(type: ComputerType) {
        when (type) {
            ComputerType.PC -> PC()
            ComputerType.SERVER -> Server()
        }
    }
}


fun p1() {
    val com = ComputerFactory().produce(ComputerType.PC)
    println(com)
}

object ComputerFactory2 {
    fun produce(type: ComputerType) {
        when (type) {
            ComputerType.PC -> PC()
            ComputerType.SERVER -> Server()
        }
    }
}

fun p2() {
    val com = ComputerFactory2.produce(ComputerType.PC)
    println(com)
}

fun p3(){

}

fun main() {
    val com = ComputerFactory2.produce(ComputerType.PC)
    println(com)
}