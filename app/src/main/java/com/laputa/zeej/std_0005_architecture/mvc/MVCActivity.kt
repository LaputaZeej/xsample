package com.laputa.zeej.std_0005_architecture.mvc

import com.laputa.zeej.std_0005_architecture.BaseActivity
import com.laputa.zeej.std_0005_architecture.model.UserModel
import com.laputa.zeej.std_0005_architecture.model.UserModelImpl
import com.laputa.zeej.std_0005_architecture.model.data.BookData
import com.laputa.zeej.std_0005_architecture.model.data.UserData
import com.laputa.zeej.std_0005_architecture.model.data.UserWithBooks
import com.laputa.zeej.std_0005_architecture.model.data.display
import java.util.concurrent.Executors

// mvc
class MVCActivity : BaseActivity() {

    private val userModel: UserModel by lazy { UserModelImpl() }
    private val sPools = Executors.newCachedThreadPool()

    override fun doAction(userId: String) {
        sPools.execute {
            try {
                runOnUiThread { showLoading(true) }

                userModel.loadUser(userId, object : UserModel.UserCallback {
                    override fun onSuccess(user: UserData?) {
                        if (user == null) {
                            complete("用户信息为空")
                            return
                        }
                        userModel.loadBook(userId, object : UserModel.BookCallback {
                            override fun onSuccess(book: List<BookData>) {
                                val result = UserWithBooks(user, book)
                                complete(result)
                            }

                            override fun onFail(e: Throwable) {
                                val result = UserWithBooks(user, listOf())
                                complete(result)
                            }

                        })
                    }

                    override fun onFail(e: Throwable) {
                        complete("用户信息异常：$e")
                    }

                })
            } catch (e: Throwable) {
                e.printStackTrace()
                complete("用户信息异常：$e")
            }

        }
    }

    private fun complete(error: String) {
        runOnUiThread {
            showUserInfo(error)
            showBookInfo("")
            showLoading(false)
        }
    }

    private fun complete(data: UserWithBooks) {
        runOnUiThread {
            showUserInfo(data.user.toString())
            showBookInfo(data.books.display())
            showLoading(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sPools.shutdown()
    }


}