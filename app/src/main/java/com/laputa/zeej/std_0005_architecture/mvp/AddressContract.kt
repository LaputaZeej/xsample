package com.laputa.zeej.std_0005_architecture.mvp

import androidx.annotation.MainThread
import com.laputa.zeej.std_0005_architecture.model.data.AddressData
import com.laputa.zeej.std_0005_architecture.mvp.core.LoadingMVPView
import com.laputa.zeej.std_0005_architecture.mvp.core.MVPPresenter

interface AddressPresenter : MVPPresenter {
    fun loadAddress(id: String)
}

interface AddressView : LoadingMVPView{
    @MainThread
    fun onAddress(address: AddressData)
    @MainThread
    fun onAddressFail(msg: String)
}