package com.laputa.zeej.std_0006_android.binder.case01

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import com.laputa.zeej.std_0006_android.binder.REQ_CODE
import com.laputa.zeej.std_0006_android.binder.StudentMap
import com.laputa.zeej.std_0006_android.binder.i

// Binder
// Binder Binder是Android中独有的一种进程间通信方式。它底层依靠mmap,只需要一次数据拷贝，把一块物理内存同时映射到内核和目标进程的用户空间。
// C-S

// Server
class GradeService : Service() {
    private val mBinder: Binder = object : Binder() {
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            when (code) {
                REQ_CODE -> {
                    val name = data.readString()
                    val studentGrade = getStudentGrade(name)
                    i("[service]$studentGrade")
                    reply?.writeInt(studentGrade)
                    return true
                }
                else -> {
                }
            }
            return super.onTransact(code, data, reply, flags)
        }

        private fun getStudentGrade(name: String?): Int {
            return StudentMap.getStudentGrade(name = name ?: "")
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }
}


