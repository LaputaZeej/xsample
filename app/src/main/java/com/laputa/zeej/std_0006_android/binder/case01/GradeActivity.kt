package com.laputa.zeej.std_0006_android.binder.case01

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import com.laputa.zeej.databinding.ActivityGradeBinding
import com.laputa.zeej.std_0006_android.binder.REQ_CODE
import com.laputa.zeej.std_0006_android.binder.e
import com.laputa.zeej.std_0006_android.binder.i
import kotlin.random.Random

class GradeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGradeBinding
    private var mRemoteBinder: IBinder? = null
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mRemoteBinder = service
            i("[client]onServiceConnected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            e("[client]onServiceConnected")
            mRemoteBinder = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGradeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            actionBind.setOnClickListener {
                bindService()
            }

            actionSelect.setOnClickListener {
                val name = "name-"+Random.nextInt(10)
                val grade = selectGrade(name)
                refreshInfo("name=$name grade=$grade")
            }
        }
    }

    private fun refreshInfo(msg: String) {
        binding.tvInfo.text = "info:$msg"
    }

    private fun selectGrade(name: String ):Int {
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        data.writeString(name)
        return try {
            mRemoteBinder?.run {
                transact(REQ_CODE, data, reply, 0)
                reply.readInt().apply {
                   i("[client]$this")
                }
            } ?: -1

        } catch (e: Throwable) {
            //e.printStackTrace()
            e("[client]${e.message}")
            -1
        }finally {
            data.recycle()
            reply.recycle()
        }
    }

    private fun bindService() {
        bindService(Intent(ACTION_GRADE).apply {
                           setPackage(packageName)
        }, mServiceConnection, BIND_AUTO_CREATE)
    }

    companion object {
        private const val ACTION_GRADE = "android.intent.action.server.xpl.gradeservice"
        fun ship(activity: Activity) {
            activity.startActivity(Intent(activity, GradeActivity::class.java))
        }
    }


}