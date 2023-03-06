package com.laputa.zeej.std_0005_architecture.mvp

import android.os.Bundle
import android.view.View
import com.laputa.zeej.R
import com.laputa.zeej.std_0005_architecture.BaseActivity
import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.model.data.UserWithBooks
import com.laputa.zeej.std_0005_architecture.model.data.display

class MVPActivity() : BaseActivity(), AddressView, UserView {
    private val presenter: UserPresenter by lazy { UserPresenterImpl(this) }

    private val addressPresenter: AddressPresenter by lazy { AddressPresenterImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addressPresenter.loadAddress("111")

        findViewById<View>(R.id.tv_address_info).setOnClickListener {
            addressPresenter.loadAddress("1111111")
        }
    }

    override fun doAction(userId: String) {
        presenter.loadUser(userId)
    }

    override fun onLoading(show: Boolean, msg: String) {
        showLoading(show = show, msg)
    }

    override fun onUserLoad(user: UserWithBooks, time: Long) {
        showUserInfo(user.user.toString())
        showBookInfo(user.books.display() + "\n 总耗时：$time ms")

    }

    override fun onUserFail(msg: String) {
        showUserInfo(msg)
        showBookInfo("")
    }

    override fun onAddress(address: AddressData) {
        showAddressInfo(address.toString())
    }

    override fun onAddressFail(msg: String) {
        showAddressInfo(msg)
    }
}