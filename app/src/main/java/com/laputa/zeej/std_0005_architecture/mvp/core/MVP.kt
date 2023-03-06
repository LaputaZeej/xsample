package com.laputa.zeej.std_0005_architecture.mvp.core

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface MVPView

interface LoadingMVPView : MVPView {
    @MainThread
    fun onLoading(show: Boolean, msg: String = "加载中...")
}

interface MVPPresenter {
    fun onClear()
}

abstract class AbstractMVPPresenter<out V : MVPView>(view: V) : LifecycleEventObserver,
    MVPPresenter {

    private val handler = Handler(Looper.getMainLooper())
    private val _view: V = view
    val view: V
        get() = _view

    init {
        Log.i("_mvp_", "init")
        if (_view is LifecycleOwner) {
            _view.lifecycle.addObserver(this)
        }
    }

    protected fun runOnUiThread(block: () -> Unit) {
        handler.post(block)
    }

    override fun onClear() {
        Log.i("_mvp_", "onClear")
        if (_view is LifecycleOwner) {
            _view.lifecycle.removeObserver(this)
        }
        handler.removeCallbacksAndMessages(null)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        onViewStateChanged(event)
        when (event) {
            Lifecycle.Event.ON_CREATE -> {

            }
            Lifecycle.Event.ON_DESTROY -> {
                onClear()
            }
            else -> {

            }
        }
    }

    open fun onViewStateChanged(event: Lifecycle.Event) {
        Log.i("_mvp_", "onViewStateChanged : $event")
    }
}