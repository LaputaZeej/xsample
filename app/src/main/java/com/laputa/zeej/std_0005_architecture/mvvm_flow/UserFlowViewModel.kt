package com.laputa.zeej.std_0005_architecture.mvvm_flow

import android.util.Log
import androidx.lifecycle.*
import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.model.data.UserWithBooks
import com.laputa.zeej.std_0005_architecture.model.repository.UserRepository
import com.laputa.zeej.std_0005_architecture.model.repository.UserRepositoryImpl
import com.laputa.zeej.std_0005_architecture.mvvm_livedata.ActionStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class UserFlowViewModel : ViewModel() {
    private val userRepository: UserRepository by lazy { UserRepositoryImpl() }

    private val _actionEffectFlow: MutableSharedFlow<ActionStatus<*>> = MutableSharedFlow()

    val actionEffectFlow = _actionEffectFlow.asSharedFlow()

    private val _userWithBooksFlow: MutableStateFlow<UserWithBooks?> = MutableStateFlow(null)
    private val _addressFlow: MutableStateFlow<AddressData?> = MutableStateFlow(null)

    private val userWithBooks: Flow<UserWithBooks?> = _userWithBooksFlow.asStateFlow()

    val userFlow = userWithBooks.map {
        it?.user
    }

    val booksLiveData = userWithBooks.map {
        it?.books ?: listOf()
    }.asLiveData()

    val addressFlow: Flow<AddressData?>
        get() = _addressFlow

    fun loadUserAndBooks(userId: String) {
        viewModelScope.launch() {
            try {
                _actionEffectFlow.emit(ActionStatus.Start(userId, "加载用户"))
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
                    _userWithBooksFlow.value = result
                    _actionEffectFlow.emit(
                        ActionStatus.Success(userId, result, "耗时：$time ms")
                    )
                } else {
                    _actionEffectFlow.emit(ActionStatus.Fail(userId, msg = "获取失败"))
                    _userWithBooksFlow.value = null
                }
            } catch (e: Throwable) {
                Log.e("_mvvm_", "catch $e")
                _userWithBooksFlow.value = null
                _actionEffectFlow.emit(ActionStatus.Fail(userId, e, e.message ?: ""))
            } finally {
                _actionEffectFlow.emit(ActionStatus.Complete(userId))
                _actionEffectFlow.emit(ActionStatus.Idle)
            }
        }
    }

    fun loadAddress(userId: String) {
        viewModelScope.launch() {
            try {
                _actionEffectFlow.emit(ActionStatus.Start(userId, "加载地址"))
                val address = withContext(Dispatchers.IO) {
                    val user = userRepository.loadUser(userId)
                        ?: throw java.lang.IllegalStateException("获取user失败")
                    userRepository.loadAddress(user.name)
                }
                _addressFlow.value = address
                _actionEffectFlow.emit(ActionStatus.Success(userId, address))
            } catch (e: Throwable) {
                Log.e("_mvvm_", "catch $e")
                _addressFlow.value = null
                _actionEffectFlow.emit(ActionStatus.Fail(userId, e, e.message ?: ""))
            } finally {
                _actionEffectFlow.emit(ActionStatus.Complete(userId))
                _actionEffectFlow.emit(ActionStatus.Idle)
            }
        }
    }

}