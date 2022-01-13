package com.laputa.zeej.std_0006_android.binder.case03

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.laputa.zeej.databinding.ActivityGradeBinding
import com.laputa.zeej.std_0006_android.binder.e
import com.laputa.zeej.std_0006_android.binder.i
import kotlin.random.Random

class AIDLGradeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGradeBinding
    private var mIUserInterface: IUserInterface? = null
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mIUserInterface = IUserInterface.Stub.asInterface(service)
            i("[client]onServiceConnected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            e("[client]onServiceConnected")
            mIUserInterface = null
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
        binding.tvInfo.text = "aidl info:$msg"
    }

    private fun selectGrade(name: String ):Int {
        return mIUserInterface?.getStudentGrade( name)?:-1
    }

    private fun bindService() {
        bindService(Intent(ACTION_GRADE).apply {
                           setPackage(packageName)
        }, mServiceConnection, BIND_AUTO_CREATE)
    }

    companion object {
        private const val ACTION_GRADE = "android.intent.action.server.xpl.aidl.aidl.service"
        fun ship(activity: Activity) {
            activity.startActivity(Intent(activity, AIDLGradeActivity::class.java))
        }
    }


}