package com.laputa.zeej.flow

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

fun <T> Flow<T>.autoCancel(owner: LifecycleOwner, block: suspend (T) -> Unit) {
    var job: Job by Delegates.notNull()
    val observer = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_CREATE -> {

                }
                Lifecycle.Event.ON_START -> {
                    job = owner.lifecycleScope.launch {
                        this@autoCancel.collect {
                            block(it)
                        }
                    }
                }
                Lifecycle.Event.ON_RESUME -> {
                }
                Lifecycle.Event.ON_PAUSE -> {
                }
                Lifecycle.Event.ON_STOP -> {
                    job.cancel()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    owner.lifecycle.removeObserver(this)
                }
                Lifecycle.Event.ON_ANY -> {
                }
            }
        }
    }
    owner.lifecycle.addObserver(observer)
}