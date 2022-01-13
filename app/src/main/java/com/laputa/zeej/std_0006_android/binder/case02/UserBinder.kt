package com.laputa.zeej.std_0006_android.binder.case02

import android.os.Binder
import android.os.Parcel
import com.laputa.zeej.std_0006_android.binder.REQ_CODE
import com.laputa.zeej.std_0006_android.binder.StudentMap
import com.laputa.zeej.std_0006_android.binder.i

class UserBinder :Binder(),UserInterface {
    override fun getStudentGrade(name: String):Int {
        return StudentMap.getStudentGrade(name)
    }

    override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
        when (code) {
            REQ_CODE -> {
                val name = data.readString()
                val studentGrade = getStudentGrade(name?:"")
                i("[service]$studentGrade")
                reply?.writeInt(studentGrade)
                return true
            }
            else -> {
            }
        }
        return super.onTransact(code, data, reply, flags)
    }
}