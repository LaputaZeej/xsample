package com.laputa.zeej.std_0005_architecture.mvp

import com.laputa.zeej.std_0005_architecture.model.UserModel
import com.laputa.zeej.std_0005_architecture.model.UserModelImpl
import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.mvp.core.AbstractMVPPresenter
import java.util.concurrent.Executors

class AddressPresenterImpl(view: AddressView) : AbstractMVPPresenter<AddressView>(view),
    AddressPresenter {
    private val sPools = Executors.newCachedThreadPool() // dai
    private val userModel: UserModel by lazy { UserModelImpl() }

    override fun loadAddress(id: String) {
        sPools.execute {
            try {
                runOnUiThread {
                    view.onLoading(true)
                }
                userModel.loadAddress(id, object : UserModel.AddressCallback {
                    override fun onSuccess(address: AddressData?) {
                        if (address == null) {
                            view.onLoading(false)
                            view.onAddressFail("地址为空")
                            return
                        }
                        view.onLoading(false)
                        view.onAddress(address)

                    }

                    override fun onFail(e: Throwable) {
                        view.onLoading(false)
                        view.onAddressFail("地址异常：$e")
                    }

                })
            } catch (e: Throwable) {
                view.onLoading(false)
                view.onAddressFail("地址异常：$e")
            }
        }
    }

    override fun onClear() {
        super.onClear()
        sPools.shutdown()
    }

}