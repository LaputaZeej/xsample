package com.laputa.zeej.std_0005_architecture.model

import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.model.data.BookData
import com.laputa.zeej.std_0005_architecture.model.data.UserData
import kotlin.random.Random

class UserModelImpl : UserModel {
    override fun loadUser(userId: String, callback: UserModel.UserCallback) {
        // 模拟获取用户信息
        try {
            if (mockOperator()) {
                callback.onSuccess(UserData("userId-${System.currentTimeMillis()}", 10))
            } else {
                callback.onFail(java.lang.IllegalStateException("找不到${userId}用户信息@${System.currentTimeMillis()}"))
            }
        } catch (e: Throwable) {
            callback.onFail(e)
        }
    }

    override fun loadBook(userId: String, callback: UserModel.BookCallback) {
        // 模拟获取用户书籍
        try {
            Thread.sleep(500)
            callback.onSuccess((1..Random.nextInt(10)).map {
                BookData("book-${it}", "author-$it @$userId")
            })
        } catch (e: Throwable) {
            callback.onFail(e)
        }
    }

    override fun loadAddress(userId: String, callback: UserModel.AddressCallback) {
        // 模拟获取用户地址
        try {
            if (mockOperator()) {
                callback.onSuccess(
                    AddressData(
                        "${userId} - address-${System.currentTimeMillis()}",
                        "address-2",
                        "address-3"
                    )
                )
            } else {
                callback.onFail(java.lang.IllegalStateException("找不到${userId}的地址"))
            }
        } catch (e: Throwable) {
            callback.onFail(e)
        }
    }

    private fun mockOperator(time: Long = 1000): Boolean {
        Thread.sleep(time)
        return Random.nextInt(10) > 3
    }

}