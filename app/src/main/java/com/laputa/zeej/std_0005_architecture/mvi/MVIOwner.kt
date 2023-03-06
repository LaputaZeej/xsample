package com.laputa.zeej.std_0005_architecture.mvi

import android.util.Log
import com.laputa.zeej.std_0010_kotlin.coroutine.jw.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class MVI<INTENT : MVIIntent, STATE : MVIState, EFFECT : MVIEffect>(private val coroutineScope: CoroutineScope) {
    private val _intent: Channel<INTENT> = Channel(Channel.UNLIMITED)

    private val _state: MutableStateFlow<Pair<Long, STATE?>> = MutableStateFlow(-1L to null)
    val state = _state.asStateFlow().map {
        it.second
    }

    private val _effect: MutableSharedFlow<EFFECT> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    init {
        _intent.receiveAsFlow().onEach {
            Log.i("_mvi_", "intent:$it")
            handle(it)
        }.launchIn(viewModelScope)
    }

    fun sendIntent(intent: INTENT) {
        viewModelScope.launch {
            _intent.send(intent)
        }
    }

    abstract fun handle(intent: INTENT)

    fun state( block: STATE?.() -> (STATE?)) {
        Log.i("_mvi_", "state")
        val old = _state.value.second
//        if (old == null) {
//            _state.value = System.currentTimeMillis() to init
//        } else {
            val cur = block(old)
            _state.value = System.currentTimeMillis() to cur
//        }

    }

    suspend fun effect(effect: EFFECT) {
        Log.i("_mvi_", "effect")
        _effect.emit(effect)
    }
}