package com.laputa.zeej.std_0010_kotlin.optin

@OptIn(UnstableApi::class,UnSupportApi::class)
//@UnSupportApi
fun main() {
    val api = Api()
    api.api01()
    api.api02()
    api.api03() // error
}