package com.laputa.zeej.std_0005_architecture.mvvm_flow

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.laputa.zeej.R
import com.laputa.zeej.std_0005_architecture.BaseActivity
import com.laputa.zeej.std_0005_architecture.model.data.display
import com.laputa.zeej.std_0005_architecture.mvvm_livedata.ActionStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MVVMFlowActivity() : BaseActivity() {

    private val userModel: UserFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.tv_address_info).setOnClickListener {
            userModel.loadAddress("Java ")
        }
        initViewModels()
    }

    private fun initViewModels() {
        userModel.userFlow.flowWithLifecycle(lifecycle).onEach {
            showUserInfo(it?.toString() ?: "")
        }.launchIn(lifecycleScope)

        userModel.booksLiveData.observe(this, Observer {
            showBookInfo(it?.display() ?: "")
        })

        userModel.addressFlow.flowWithLifecycle(lifecycle).onEach {
            showAddressInfo(it?.toString() ?: "")
        }.launchIn(lifecycleScope)

        userModel.actionEffectFlow.flowWithLifecycle(lifecycle).onEach {
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
        }.launchIn(lifecycleScope)
    }

    override fun doAction(userId: String) {
        userModel.loadUserAndBooks(userId)
    }
}

