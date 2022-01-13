package com.laputa.zeej.std_0006_android.binder

object StudentMap {

    private val map:MutableMap<String,UserInfo> = mutableMapOf()

    init {
        (1..10).forEach {
            val name = "name-$it"
            map[name] = UserInfo(name,it)
        }
    }

    fun getStudentGrade(name:String):Int{
        return map[name]?.grade?:0
    }
}