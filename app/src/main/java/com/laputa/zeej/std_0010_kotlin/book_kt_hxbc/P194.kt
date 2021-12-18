package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

internal open class Base {
    open fun bar() {
        println("Base::bar")
    }
}

internal class Foo : Base() {
    override fun bar() {
        println("Foo::bar")
    }
}

internal fun Base.foo() = println("Base.foo")
internal fun Foo.foo() = println("Foo.foo")

// 调度接收器和扩展接收器
internal class Y
internal open class X {
    fun Y.foo() = " i am Y.foo"

    open fun Base.foo(){
        println("i am Base.foo in X.")
    }

    open fun Foo.foo(){
        println("i am Foo.foo in X.")
    }

    fun deal(base:Base){
        base.foo()
    }
}

internal class Z:X(){
    override fun Base.foo() {
        println("i am Base.foo in Z.")
    }

    override fun Foo.foo() {
        println("i am Foo.foo in Z.")
    }
}

fun main() {
//    val s1 = Foo()
//    val s2: Base = Foo()
//    s1.foo()
//    s2.foo()
//    println("------------")
//    s1.bar()
//    s2.bar()
//    println("------------")
//    val y = Y()
//    val x = X()
//    val result = x.run {
//        y.foo()
//    }
//    println(result)
    X().deal(Base()) // i am Base.foo in X.
    Z().deal(Base()) // i am Base.foo in Z. // 被动态处理 dispatch receiver
    X().deal(Foo())  // i am Base.foo in X. // 被静态处理 extension receiver
    Z().deal(Foo())  // i am Base.foo in Z.
}