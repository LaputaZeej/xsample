package com.laputa.zeej.std_0010_kotlin.delegate

import kotlin.reflect.KProperty

private class Delegate01 {
    var message: String = "01"
}

private class TestDelegate {
    private val delegate01: Delegate01 = Delegate01()
    var message: String
        get() = delegate01.message
        set(value) {
            delegate01.message = value
        }

    var message02: String by PropertyDelegateX()

    val message03 :String by lazy {
        "message03"
    }

}

class PropertyDelegateX {
    var message: String = "02"
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {

        return "$thisRef, thank you for delegating '${property.name}' to me! $message"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
        message = value
    }
}

fun main() {
    val test = TestDelegate()

    println(test.message)
    println(test.message02)
    test.message = "0----------"
    test.message02 = "2----------"
    println(test.message)
    println(test.message02)

}