package com.laputa.zeej.std_0005_architecture.mvi

import android.util.Log
import androidx.lifecycle.*
import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.model.data.UserWithBooks
import com.laputa.zeej.std_0005_architecture.model.repository.UserRepository
import com.laputa.zeej.std_0005_architecture.model.repository.UserRepositoryImpl
import com.laputa.zeej.std_0005_architecture.mvvm_livedata.ActionStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map


class UserMVIViewModel() : ViewModel() {
    sealed interface Intent : MVIIntent {
        data class LoadUser(val userId: String) : Intent
        data class LoadAddress(val userId: String) : Intent
    }

    data class State(val user: UserWithBooks?=null, val address: AddressData?=null) : MVIState

    sealed interface Effect : MVIEffect {
        data class UserActionStatusEffect<T>(val actionStatus: ActionStatus<T>) : Effect
        data class AddressActionStatusEffect<T>(val actionStatus: ActionStatus<T>) : Effect
    }

    private val userRepository: UserRepository by lazy { UserRepositoryImpl() }

    val mvi: MVI<Intent, State, Effect> =
        object : MVI<Intent, State, Effect>(viewModelScope) {
            override fun handle(intent: Intent) {
                when (intent) {
                    is Intent.LoadAddress -> doLoadAddress(intent.userId)
                    is Intent.LoadUser -> doLoadUser(intent.userId)
                }
            }

        }

    val userFlow = mvi.state.map {
        Log.i("_mvi_", "u->${ it?.user?.user}")
        it?.user?.user
    }

    val booksFlow = mvi.state.map {
        Log.i("_mvi_", "b->${ it?.user?.books}")
        (it?.user?.books ?: listOf()) to System.currentTimeMillis()
    }

    val addressFlow = mvi.state.map {
        it?.address
    }

    val effect = mvi.effect

    private fun doLoadAddress(userId: String) {

    }

    private fun doLoadUser(userId: String) {
        viewModelScope.launch() {
            try {
                mvi.effect(Effect.UserActionStatusEffect(ActionStatus.Start(userId, "加载用户")))
                val start = System.currentTimeMillis()
                val userDeferred =
                    async(Dispatchers.IO) {
                        kotlin.runCatching {
                            userRepository.loadUser(userId)
                        }
                    }
                val booksDeferred =
                    async(Dispatchers.IO) {
                        kotlin.runCatching {
                            userRepository.loadBooks(userId)
                        }
                    }
                val user = userDeferred.await().getOrThrow()
                val books = booksDeferred.await().getOrThrow()
                if (user != null) {
                    val time = System.currentTimeMillis() - start
                    val result = UserWithBooks(user, books)
                    mvi.state {
                        this?.copy(user = result)?: State(user = result)
                    }
                    mvi.effect(
                        Effect.UserActionStatusEffect(
                            ActionStatus.Success(
                                userId,
                                result,
                                "耗时：$time ms"
                            )
                        )
                    )
                } else {
                    mvi.state {
                        null
                    }
                    mvi.effect(
                        Effect.UserActionStatusEffect(
                            ActionStatus.Fail(
                                userId,
                                msg = "获取失败"
                            )
                        )
                    )
                }
            } catch (e: Throwable) {
                Log.e("_mvvm_", "catch $e")
                mvi.state {
                    null
                }
                mvi.effect(
                    Effect.UserActionStatusEffect(
                        ActionStatus.Fail(
                            userId,
                            e,
                            e.message ?: ""
                        )
                    )
                )
            } finally {
                mvi.effect(Effect.UserActionStatusEffect(ActionStatus.Complete(userId)))
                mvi.effect(Effect.UserActionStatusEffect(ActionStatus.Idle))
            }
        }
    }


//    fun loadUserAndBooks(userId: String) {
//        viewModelScope.launch() {
//            try {
//                _actionEffectLiveData.value = ActionStatus.Start(userId, "加载用户")
//                val start = System.currentTimeMillis()
//                val userDeferred =
//                    async(Dispatchers.IO) {
//                        kotlin.runCatching {
//                            userRepository.loadUser(userId)
//                        }
//                    }
//                val booksDeferred =
//                    async(Dispatchers.IO) {
//                        kotlin.runCatching {
//                            userRepository.loadBooks(userId)
//                        }
//                    }
//                val user = userDeferred.await().getOrThrow()
//                val books = booksDeferred.await().getOrThrow()
//                if (user != null) {
//                    val time = System.currentTimeMillis() - start
//                    val result = UserWithBooks(user, books)
//                    _userWithBooksLiveData.value = result
//                    _actionEffectLiveData.value =
//                        ActionStatus.Success(userId, result, "耗时：$time ms")
//                } else {
//                    _actionEffectLiveData.value = ActionStatus.Fail(userId, msg = "获取失败")
//                    _userWithBooksLiveData.value = null
//                }
//            } catch (e: Throwable) {
//                Log.e("_mvvm_", "catch $e")
//                _userWithBooksLiveData.value = null
//                _actionEffectLiveData.value = ActionStatus.Fail(userId, e, e.message ?: "")
//            } finally {
//                _actionEffectLiveData.value = ActionStatus.Complete(userId)
//                _actionEffectLiveData.value = ActionStatus.Idle
//            }
//        }
//    }
//
//    fun loadAddress(userId: String) {
//        viewModelScope.launch() {
//            try {
//                _actionEffectLiveData.value = ActionStatus.Start(userId, "加载地址")
//                val address = withContext(Dispatchers.IO) {
//                    val user = userRepository.loadUser(userId)
//                        ?: throw java.lang.IllegalStateException("获取user失败")
//                    userRepository.loadAddress(user.name)
//                }
//                _addressLiveData.value = address
//                _actionEffectLiveData.value = ActionStatus.Success(userId, address)
//            } catch (e: Throwable) {
//                Log.e("_mvvm_", "catch $e")
//                _addressLiveData.value = null
//                _actionEffectLiveData.value = ActionStatus.Fail(userId, e, e.message ?: "")
//            } finally {
//                _actionEffectLiveData.value = ActionStatus.Complete(userId)
//                _actionEffectLiveData.value = ActionStatus.Idle
//            }
//        }
//    }

}