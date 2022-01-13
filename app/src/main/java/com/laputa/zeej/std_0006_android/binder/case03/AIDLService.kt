package com.laputa.zeej.std_0006_android.binder.case03

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.laputa.zeej.std_0006_android.binder.StudentMap

class AIDLService : Service() {

    override fun onBind(intent: Intent?): IBinder {
        return object : IUserInterface.Stub() {
            override fun basicTypes(
                anInt: Int,
                aLong: Long,
                aBoolean: Boolean,
                aFloat: Float,
                aDouble: Double,
                aString: String?
            ) {

            }

            override fun getStudentGrade(name: String?): Int {
                return StudentMap.getStudentGrade(name = name ?: "")
            }

        }
    }
}