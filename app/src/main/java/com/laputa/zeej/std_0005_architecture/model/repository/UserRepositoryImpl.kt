package com.laputa.zeej.std_0005_architecture.model.repository

import com.laputa.zeej.std_0005_architecture.model.UserModel
import com.laputa.zeej.std_0005_architecture.model.UserModelImpl
import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.model.data.BookData
import com.laputa.zeej.std_0005_architecture.model.data.UserData
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepositoryImpl : UserRepository {
    private val userModel: UserModel by lazy { UserModelImpl() }
    override suspend fun loadUser(userId: String): UserData? {
        return suspendCancellableCoroutine {
            try {
                userModel.loadUser(userId, object : UserModel.UserCallback {
                    override fun onSuccess(user: UserData?) {
                        it.resume(user)
                    }

                    override fun onFail(e: Throwable) {
                        it.resumeWithException(e)
                    }
                })
            }catch (e:Throwable){
                it.resumeWithException(e)
            }

        }
    }

    override suspend fun loadBooks(userId: String): List<BookData> {
        return suspendCancellableCoroutine {
            try {
                userModel.loadBook(userId, object : UserModel.BookCallback {
                    override fun onSuccess(book: List<BookData>) {
                        it.resume(book)
                    }

                    override fun onFail(e: Throwable) {
                        it.resumeWithException(e)
                    }
                })
            } catch (e: Throwable) {
                it.resumeWithException(e)
            }
        }
    }

    override suspend fun loadAddress(userId: String): AddressData? {
        return suspendCancellableCoroutine {
            try {
                userModel.loadAddress(userId, object : UserModel.AddressCallback {
                    override fun onSuccess(address: AddressData?) {
                        it.resume(address)
                    }

                    override fun onFail(e: Throwable) {
                        it.resumeWithException(e)
                    }
                })
            }catch (e:Throwable){
                it.resumeWithException(e)
            }

        }
    }


}