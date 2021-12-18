package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc


private var aaaaa = 22

// 一旦一个函数被定义为内联函数，便不能获取闭包类的私有成员，除非你把他们声明为internal
internal class P168 {
    private var name: String = "kt"
    private var age = 1

    fun noInLineTest(block: () -> Unit) {
        println("name =$name")
        println("age =$age")
        println("aaaaa =$aaaaa")
    }

    inline fun test(block: () -> Unit, noinline nb: () -> Unit) {
        println("name =$name") // 私有成员 error
        println("age =$age")
        println(" aaaaa=$aaaaa")
    }
}

// 非局部返回
private fun localReturn() {
    return
}

private fun foo() {
    println("before")
    localReturn()
    println("after")
}

private fun foo2(block: () -> Unit) {
    println("before")
    block()
    println("after")
}

private inline fun foo3(block: () -> Unit) {
    println("before")
    block()
    println("after")
}

private fun foo4(block: () -> Unit) {
    println("before")
    block()
    println("after")
    return
}

private inline fun foo5(crossinline block: () -> Unit) {
    println("before")
    block()
    println("after")
}

private fun foo6(): Boolean {
    (1..20).forEach {
        println("it=$it")
        if (it % 5 == 0) return true
    }
    return false
}

private fun foo7() {
    (1..20).forEach {
        println("it=$it")
        if (it % 5 == 0) return@forEach
    }
}

fun main() {
    //foo()

//    foo2 {
//        return // 'return' is not allowed here
//    }

//    foo3 { return }

    foo4 {
        return@foo4
    }

//    foo5{
//        return // 'return' is not allowed here
//    }

//    foo6()

    foo7()

    println("main end")
}
