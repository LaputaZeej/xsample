package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.*

// 元编程

object Mapper {
    fun <A : Any> toMap(a: A): Map<String, Any?> {
        return a::class.memberProperties.map { m ->
            val p = m as KProperty<*>
            p.name to p.call(a)
        }.toMap()
    }
}

data class Student(val name: String, val age: Int, var address: String) {
    fun sList(): List<Student> {
        return listOf()
    }

    fun <A> get(a: A): A {
        return a
    }
}

open class ABC<T> {
    val aa: String = ""
    fun funA(a: T) {

    }

    companion object {
        fun funB() {}
    }
}

object AABBCC : ABC<String>()

val ABC<*>.nickName
    get() = "$aa+1"

fun ABC<*>.show() {
    println(this)
}


// KClass
private fun tKClass(kClass: KClass<*> = ABC::class) {
    println("----------------KClass----------")
    println("isCompanion = ${kClass.isCompanion}")
    println("isData = ${kClass.isData}")
    println("isSealed = ${kClass.isSealed}")
    println("objectInstance = ${kClass.objectInstance}")
    println("companionObjectInstance = ${kClass.companionObjectInstance}")
    println("declaredMemberExtensionProperties = ${kClass.declaredMemberExtensionProperties.map { it.name }}")
    println("declaredMemberExtensionFunctions = ${kClass.declaredMemberExtensionFunctions.map { it.name }}")
    println("memberExtensionFunctions = ${kClass.memberExtensionFunctions.map { it.name }}")
    println("memberExtensionProperties = ${kClass.memberExtensionProperties.map { it.name }}")
    println("starProjectedType = ${kClass.starProjectedType}")
}

// KCallable
private fun tKCallable(kCallable: KCallable<*>) {
    println("----------------KCallable----------")
    println("isAbstract = ${kCallable.isAbstract}")
    println("isFinal = ${kCallable.isFinal}")
    println("isOpen = ${kCallable.isOpen}")
    println("name = ${kCallable.name}")
    println("parameters = ${kCallable.parameters.map { it.name }}")
    println("returnType = ${kCallable.returnType}")
    println("typeParameters = ${kCallable.typeParameters.map { it.name }}")
    println("visibility = ${kCallable.visibility}")
}

sealed class Nat {
    companion object {
        object Zero : Nat()
    }

    val Companion._0
        get() = Zero

    fun <A : Nat> Succ<A>.proceed(): A {
        return this.prev
    }
}

data class Succ<N : Nat>(val prev: N) : Nat()

fun <A : Nat> Nat.plus(other: A): Nat = when (other) {
    is Succ<*> -> Succ(this.plus(other.prev))
    else -> this
}

private fun kMutablePropertyShow() {
    println("----------------kMutablePropertyShow----------")
    val p = Student("kt", 8, "NB")
    val props = p::class.memberProperties
    for (prop in props) {
        when (prop) {
            is KMutableProperty<*> -> prop.setter.call(p, "艾泽拉斯")
            else -> prop.call(p)
        }
    }
    println(p.address)
}

// KParameter
private fun tKParameter() {
    println("----------------tKParameter----------")
    for (prop in Student::class.members) {
        print("${prop.name} -> ")
        for (p in prop.parameters) {
            print("${p.type} ,")
        }
        println()
    }
}

//KType
private fun tKType() {
    println("----------------tKType----------")
    Student::class.members.forEach {
        println("${it.name} -> ${it.returnType.classifier} ## ${it.returnType.arguments} ## ${it.returnType.isMarkedNullable}")
    }
}

//KTypeParameters
private fun tKTypeParameters(){
    println("----------------tKTypeParameters----------")
    for (c in Student::class.members){
        if (c.name=="get"){
            println(c.typeParameters)
        }
    }

    val list = listOf("kt")
    println(list::class.typeParameters)
}


fun main() {
    val map = Mapper.toMap(Student("kt", 22, "NingBo"))
    println(map)

    val abc = ABC<String>()
    abc.nickName
    abc.show()
    val kClass = ABC::class
    tKClass(kClass)

    tKClass(Nat::class)

    val _1 = Succ(Nat.Companion.Zero)
    val proceed = _1::class.members.find { it.name == "proceed" }
    proceed?.run {
        tKCallable(this)
        val r = this.call(_1, _1)
        println("r=$r")

    }

    kMutablePropertyShow()

    tKParameter()

    tKType()

    tKTypeParameters()
}

