package com.laputa.zeej.std_0005_architecture.mvvm_livedata

sealed class ActionStatus<out T>(val input: T, val msg: String = "") {
    object Idle: ActionStatus<Unit>(Unit)
    class Start<T>(input: T, msg: String = "") : ActionStatus<T>(input, msg)
    class Fail<T>(input: T, val e: Throwable? =null, msg: String = "") : ActionStatus<T>(input, msg)
    class Success<T, R>(input: T, val output: R, msg: String = "") : ActionStatus<T>(input, msg)
    class Complete<T>(input: T) : ActionStatus<T>(input)
}