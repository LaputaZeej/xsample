package com.laputa.zeej.std_0010_kotlin.partialfunc

// PartialFunction偏函数
// 概念

fun main() {
//    try {
//        val result = validateNumber(9)
//        println("result=$result")
//    } catch (e: Throwable) {
//        e.printStackTrace()
//    }
//
//    try {
//        val partialFunction = PartialFunction<Int, Boolean>({
//            it > 10
//        }) {
//            it > 10
//        }
//        val result = partialFunction(9)
//        println("result=$result")
//    } catch (e: Throwable) {
//        e.printStackTrace()
//    }

    val groupLeader = PartialFunction<ApplyEvent,Unit>({
        it.money<=100
    }){
          println("GroupLeader 同意 $it ")
    }

    val presidentLeader = PartialFunction<ApplyEvent,Unit>({
        it.money<=500
    }){
        println("PresidentLeader 同意 $it ")
    }

    val collage = PartialFunction<ApplyEvent,Unit>({
        it.money<=1000
    }){
        println("collage 同意 $it ")
    }

    val partialFunction = groupLeader orElse presidentLeader orElse collage
    partialFunction(ApplyEvent(10,"买铅笔"))
    partialFunction(ApplyEvent(200,"团建"))
    partialFunction(ApplyEvent(600,"组织比赛"))
    partialFunction(ApplyEvent(1200,"旅游"))
}

fun validateNumber(x: Int): Boolean {
    return if (x > 10) true else throw IllegalArgumentException("number必须大于10")
}

// 偏函数
class PartialFunction<in P1, out R>(
    private val defineAt: (P1) -> Boolean, // 校验函数
    private val f: (P1) -> R // 处理函数
) : (P1) -> R {
    override fun invoke(p1: P1): R {
        if (defineAt(p1)) {
            return f(p1)
        } else {
            throw java.lang.IllegalArgumentException("Value : [$p1] isn't supported by this function")
        }
    }

    fun isDefineAt(p1: P1) = defineAt(p1)
}

infix fun <P1, R> PartialFunction<P1, R>.orElse(/*下一个处理者*/that: PartialFunction<P1, R>): PartialFunction<P1, R> {
    return PartialFunction({ this.isDefineAt(it) || that.isDefineAt(it) }) {
        when {
            this.isDefineAt(it) -> this.invoke(it)
            else -> that.invoke(it)
        }
    }
}








