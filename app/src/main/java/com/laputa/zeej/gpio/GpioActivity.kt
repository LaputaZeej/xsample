package com.laputa.zeej.gpio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.laputa.zeej.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File

class GpioActivity : AppCompatActivity() {

    private val gpioManager: GpioManager by lazy {
        GpioManager()
    }
    private val gpioViewModel: GpioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpio)

        findViewById<View>(R.id.action_on).setOnClickListener {
            doAction(true)
        }

        findViewById<View>(R.id.action_off).setOnClickListener {
//            doAction(false)
            gpioViewModel.test()
        }

        gpioViewModel.result.flowWithLifecycle(lifecycle = lifecycle).onEach {

            refreshInfo(it)


        }.launchIn(lifecycleScope)

        var device: File = File("")
        lifecycleScope.launch {
            var set = true
            try {
                /* Check access permission */if (!device.canRead() || !device.canWrite()) {
//                    try {
//                        /* Missing read/write permission, trying to chmod the file */
//                        val su: Process = Runtime.getRuntime().exec("/system/xbin/su")
//                        val cmd = "chmod 666 ${device.absolutePath}\nexit"
//                        su.outputStream.write(cmd.toByteArray())
//                        if (su.waitFor() != 0 || !device.canRead()
//                            || !device.canWrite()
//                        ) {
//                            throw SecurityException()
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        throw SecurityException()
//                    }
                }
                while (true) {
                    delay(1000)
                    set = !set
                    doAction(set)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun doAction(on: Boolean) {
        val t: String = findViewById<EditText>(R.id.et_gpio).text.toString()
        try {
            val gpio = t.toInt()
            val result = gpioManager.setGpios(gpio, if (on) 1 else 0)
            val msg = "设置GPIO:${gpio}结果:$result"
            refreshInfo(msg)
        } catch (e: Throwable) {
            e.printStackTrace()
            refreshInfo("error : $e")
        }
    }

    private fun refreshInfo(msg: String) {
        findViewById<TextView>(R.id.tv_Info).append("$msg\n")
        Log.e("gpio_","$msg")
    }

    companion object {
        fun ship(activity: Activity) {
            activity.startActivity(Intent(activity, GpioActivity::class.java))
        }
    }
}