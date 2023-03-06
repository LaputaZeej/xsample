package com.laputa.zeej.std_0005_architecture.model

import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.model.data.BookData
import com.laputa.zeej.std_0005_architecture.model.data.UserData

interface UserModel {
    fun loadUser(userId: String, callback: UserCallback)
    fun loadBook(userId: String, callback: BookCallback)
    fun loadAddress(userId: String, callback: AddressCallback)

    interface UserCallback {
        fun onSuccess(user: UserData?)
        fun onFail(e: Throwable)
    }

    interface BookCallback {
        fun onSuccess(book: List<BookData>)
        fun onFail(e: Throwable)
    }

    interface AddressCallback {
        fun onSuccess(address: AddressData?)
        fun onFail(e: Throwable)
    }
}