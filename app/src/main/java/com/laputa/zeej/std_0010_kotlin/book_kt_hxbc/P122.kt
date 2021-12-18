package com.laputa.zeej.std_0010_kotlin.book_kt_hxbc

class SmartClass {
    var name: String? = null
    val address: String?
        get() = null

    fun test() {
        var color:String? = null

        if (name != null) {
           // name.length
        }
        color=""
        if (address != null) {
           // address.length
        }
        if (color != null) {
            color.length
        }
    }
}

fun main() {
    try {
        val a:String? = cast<String>(22)
        println(a)
    }catch (e:Throwable){
        e.printStackTrace()
    }

    try {
        val r:Long? = cast<Long>("22333aaaa33Lbbb")
        println(r)
    }catch (e:Throwable){
        e.printStackTrace()
    }


    println(cast2<Double>(2233333.0))
}

private fun <T> cast(a:Any):T? = a as? T
private inline fun <reified T> cast2(a:Any):T? = a as? T