package com.laputa.zeej.std_0006_android.binder.case02

import android.os.IBinder
import android.os.Parcel
import com.laputa.zeej.std_0006_android.binder.REQ_CODE
import com.laputa.zeej.std_0006_android.binder.e
import com.laputa.zeej.std_0006_android.binder.i

// 客户端优化的思路是在连接到远程服务时候实例化一个代理类，代理类持有Binder，让代理类行使Binder的权利
class BinderProxy(val binder: IBinder?) : UserInterface {
    companion object {
        fun asInterface(iBinder: IBinder?): UserInterface? {
            return when (iBinder) {
                null -> null
                is UserInterface ->{
                    i("[client]当前进程")
                    iBinder
                }
                else ->           {i("[service]远程进程");BinderProxy(iBinder)}
            }
        }
    }

    override fun getStudentGrade(name: String): Int {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        data.writeString(name)
        return try {
            binder?.run {
                transact(REQ_CODE, data, reply, 0)
                reply.readInt().apply {
                    i("[client]$this")
                }
            } ?: -1

        } catch (e: Throwable) {
            //e.printStackTrace()
            e("[client]${e.message}")
            -1
        } finally {
            data.recycle()
            reply.recycle()
        }
    }

}