package com.laputa.zeej.std_0010_kotlin.kt16

abstract class Builder<B : Builder<B>> {
    protected val self: B = this as B
    private var id: String? = null
    fun setId(id: String): B {
        this.id = id
        return self
    }
}

open class ABuilder<B : ABuilder<B>> : Builder<B>() {
    private var name: String? = null

    fun setName(name: String): B {
        this.name = name
        return self
    }
}

class BBuilder : ABuilder<BBuilder>() {
    private var age: Int = 0
    fun setAge(age: Int): BBuilder {
        this.age = age
        return this
    }
}

fun main() {
    //val s0 = ABuilder().setId("12131").setName("he")
    val s:ABuilder<*> = ABuilder().setId("12131").setName("he") // 1.6 之前不行。
    val s2:BBuilder = BBuilder().setId("12131").setName("he").setAge(1)

}