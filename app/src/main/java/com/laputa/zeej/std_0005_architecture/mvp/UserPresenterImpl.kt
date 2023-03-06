package com.laputa.zeej.std_0005_architecture.mvp

import com.laputa.zeej.std_0005_architecture.model.UserModel
import com.laputa.zeej.std_0005_architecture.model.UserModelImpl
import com.laputa.zeej.std_0005_architecture.model.data.BookData
import com.laputa.zeej.std_0005_architecture.model.data.UserData
import com.laputa.zeej.std_0005_architecture.model.data.UserWithBooks
import com.laputa.zeej.std_0005_architecture.mvp.core.AbstractMVPPresenter
import java.util.concurrent.Executors

class UserPresenterImpl(userView: UserView) : AbstractMVPPresenter<UserView>(userView),
    UserPresenter {
    private val sPools = Executors.newCachedThreadPool()
    private val userModel: UserModel by lazy { UserModelImpl() }

    override fun loadUser(id: String) {
        sPools.execute {
            try {
                runOnUiThread {
                    view.onLoading(true)
                }
                val start = System.currentTimeMillis()
                userModel.loadUser(id, object : UserModel.UserCallback {
                    override fun onSuccess(user: UserData?) {
                        if (user == null) {
                            runOnUiThread {
                                view.onLoading(false)
                                view.onUserFail("用户信息为空")
                            }
                            return
                        }
                        userModel.loadBook(id, object : UserModel.BookCallback {
                            override fun onSuccess(book: List<BookData>) {
                                val result = UserWithBooks(user, book)
                                val time  = System.currentTimeMillis()-start
                                runOnUiThread {
                                    view.onLoading(false)
                                    view.onUserLoad(result,time)

                                }
                            }

                            override fun onFail(e: Throwable) {
                                val result = UserWithBooks(user, listOf())
                                runOnUiThread {
                                    view.onLoading(false)
                                    view.onUserFail(result.toString())
                                }
                            }

                        })
                    }

                    override fun onFail(e: Throwable) {
                        runOnUiThread {
                            view.onLoading(false)
                            view.onUserFail("用户信息异常：$e")
                        }
                    }

                })

            } catch (e: Throwable) {
                runOnUiThread {
                    view.onLoading(false)
                    view.onUserFail("用户信息异常：$e")
                }
            }
        }
    }

    override fun onClear() {
        super.onClear()
        sPools.shutdown()
    }

}