package com.laputa.zeej.std_0005_architecture.mvvm_livedata

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.laputa.zeej.R
import com.laputa.zeej.std_0005_architecture.BaseActivity
import com.laputa.zeej.std_0005_architecture.model.data.display
import kotlinx.coroutines.*
import java.lang.IllegalStateException

class MVVMActivity() : BaseActivity() {

    private val userModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.tv_address_info).setOnClickListener {
            userModel.loadAddress("Java ")
        }
        initViewModels()
    }

    private fun initViewModels() {
        userModel.userLiveData.observe(this, Observer {
            showUserInfo(it?.toString() ?: "")
        })

        userModel.booksLiveData.observe(this, Observer {
            showBookInfo(it?.display() ?: "")
        })

        userModel.addressLiveData.observe(this, Observer {
            showAddressInfo(it?.toString() ?: "")
        })

        userModel.actionEffectLiveData.observe(this, Observer {
            when (it) {
                is ActionStatus.Idle -> {

                }
                is ActionStatus.Start<*> -> {
                    showLoading(true, it.msg)
                }

                is ActionStatus.Success<*, *> -> {
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                }

                is ActionStatus.Fail<*> -> {
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                }

                is ActionStatus.Complete<*> -> {
                    showLoading(false)
                }

            }
        })
    }

    override fun doAction(userId: String) {
        userModel.loadUserAndBooks(userId)
//        debug()

    }

    private fun debug() {
        val job = lifecycleScope.launch {
            val d1: Deferred<Int> = async(SupervisorJob()) {
                while (true){
                    delay(100)
                    Log.e("result","d1 d1")
                }
                throw IllegalStateException("11111")
            }
            val d2: Deferred<Int> = async {
                delay(2000)
                12
            }
            d1.invokeOnCompletion {
                Log.e("result", "d1 cancel")
            }
            d2.invokeOnCompletion {
                Log.e("result", "d2 cancel")
            }
            try {
                val result = d1.await() + d2.await()
                Log.i("result", "result=$result")
            } catch (e: Throwable) {
                Log.e("result", "result=$e")
                e.printStackTrace()
            }
        }
        job.invokeOnCompletion {
            Log.e("result", "job cancel")
        }
        lifecycleScope.launch {
            delay(500)
            job.cancel()
        }
    }

}

