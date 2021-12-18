package com.laputa.zeej.std_0010_kotlin.deprecated

object Api {
    fun newFun(key: String, value: String) {

    }

    @Deprecated(
        message = "use newFun instead.", replaceWith = ReplaceWith(
                expression = "newFun(key,value)"
        )
    )
    fun oldFun1(key: String, value: String) {

    }

    @Deprecated(message = "use newFun instead.",replaceWith = ReplaceWith(
        expression = "Api2.newFun(key,value)",
        imports = ["com.laputa.zeej.std_0006_kotlin.deprecated.v2.Api2"]
    ))
    fun oldFun2(key: String, value: String) {

    }
}