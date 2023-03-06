package com.laputa.zeej.std_0005_architecture.mvvm_livedata

import android.util.Log
import androidx.lifecycle.*
import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.model.data.UserWithBooks
import com.laputa.zeej.std_0005_architecture.model.repository.UserRepository
import com.laputa.zeej.std_0005_architecture.model.repository.UserRepositoryImpl
import kotlinx.coroutines.*


class UserViewModel : ViewModel() {
    private val userRepository: UserRepository by lazy { UserRepositoryImpl() }

    private val _actionEffectLiveData: MutableLiveData<ActionStatus<*>> =
        MutableLiveData(ActionStatus.Idle)

    val actionEffectLiveData = _actionEffectLiveData

    private val _userWithBooksLiveData: MutableLiveData<UserWithBooks?> = MutableLiveData(null)
    private val _addressLiveData: MutableLiveData<AddressData?> = MutableLiveData(null)

    private val userWithBooks: LiveData<UserWithBooks?> = _userWithBooksLiveData

    val userLiveData = userWithBooks.map {
        it?.user
    }

    val booksLiveData = userWithBooks.map {
        it?.books ?: listOf()
    }

    val addressLiveData: LiveData<AddressData?> = _addressLiveData

    fun loadUserAndBooks(userId: String) {
        viewModelScope.launch() {
            try {
                _actionEffectLiveData.value = ActionStatus.Start(userId, "加载用户")
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
                    _userWithBooksLiveData.value = result
                    _actionEffectLiveData.value =
                        ActionStatus.Success(userId, result, "耗时：$time ms")
                } else {
                    _actionEffectLiveData.value = ActionStatus.Fail(userId, msg = "获取失败")
                    _userWithBooksLiveData.value = null
                }
            } catch (e: Throwable) {
                Log.e("_mvvm_", "catch $e")
                _userWithBooksLiveData.value = null
                _actionEffectLiveData.value = ActionStatus.Fail(userId, e, e.message ?: "")
            } finally {
                _actionEffectLiveData.value = ActionStatus.Complete(userId)
                _actionEffectLiveData.value = ActionStatus.Idle
            }
        }
    }

    fun loadAddress(userId: String) {
        viewModelScope.launch() {
            try {
                _actionEffectLiveData.value = ActionStatus.Start(userId, "加载地址")
                val address = withContext(Dispatchers.IO) {
                    val user = userRepository.loadUser(userId)
                        ?: throw java.lang.IllegalStateException("获取user失败")
                    userRepository.loadAddress(user.name)
                }
                _addressLiveData.value = address
                _actionEffectLiveData.value = ActionStatus.Success(userId, address)
            } catch (e: Throwable) {
                Log.e("_mvvm_", "catch $e")
                _addressLiveData.value = null
                _actionEffectLiveData.value = ActionStatus.Fail(userId, e, e.message ?: "")
            } finally {
                _actionEffectLiveData.value = ActionStatus.Complete(userId)
                _actionEffectLiveData.value = ActionStatus.Idle
            }
        }
    }

}