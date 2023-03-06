package com.laputa.zeej.std_0005_architecture.mvi

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.laputa.zeej.R
import com.laputa.zeej.std_0005_architecture.BaseActivity
import com.laputa.zeej.std_0005_architecture.model.data.display
import com.laputa.zeej.std_0005_architecture.mvvm_livedata.ActionStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MVIActivity() : BaseActivity() {

    private val userModel: UserMVIViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.tv_address_info).setOnClickListener {
            userModel.mvi.sendIntent(UserMVIViewModel.Intent.LoadUser("Java"))
        }
        initViewModels()
    }

    private fun initViewModels() {
        userModel.userFlow.flowWithLifecycle(lifecycle).onEach {
            showUserInfo(it.toString() ?: "")
        }.launchIn(lifecycleScope)

        userModel.booksFlow.flowWithLifecycle(lifecycle).onEach {
            showBookInfo(it.first.display() )
        }.launchIn(lifecycleScope)

        userModel.addressFlow.flowWithLifecycle(lifecycle).onEach {
            showAddressInfo(it?.toString() ?: "")
        }.launchIn(lifecycleScope)

        userModel.effect.flowWithLifecycle(lifecycle).onEach {
            when (val effect = it) {
                is UserMVIViewModel.Effect.AddressActionStatusEffect<*> -> {
                    when (val status = effect.actionStatus) {
                        is ActionStatus.Idle -> {

                        }
                        is ActionStatus.Start<*> -> {
                            showLoading(true, status.msg)
                        }

                        is ActionStatus.Success<*, *> -> {
                            Toast.makeText(this, status.msg, Toast.LENGTH_SHORT).show()
                        }

                        is ActionStatus.Fail<*> -> {
                            Toast.makeText(this, status.msg, Toast.LENGTH_SHORT).show()
                        }

                        is ActionStatus.Complete<*> -> {
                            showLoading(false)
                        }
                    }
                }
                is UserMVIViewModel.Effect.UserActionStatusEffect<*> -> {
                    when (val status = effect.actionStatus) {
                        is ActionStatus.Idle -> {

                        }
                        is ActionStatus.Start<*> -> {
                            showLoading(true, status.msg)
                        }

                        is ActionStatus.Success<*, *> -> {
                            Toast.makeText(this, status.msg, Toast.LENGTH_SHORT).show()
                        }

                        is ActionStatus.Fail<*> -> {
                            Toast.makeText(this, status.msg, Toast.LENGTH_SHORT).show()
                        }

                        is ActionStatus.Complete<*> -> {
                            showLoading(false)
                        }
                    }
                }
            }

        }.launchIn(lifecycleScope)
    }

    override fun doAction(userId: String) {
        userModel.mvi.sendIntent(UserMVIViewModel.Intent.LoadUser(userId))
    }
}

