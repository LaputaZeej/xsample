package com.laputa.zeej.std_0005_architecture.mvp

import androidx.annotation.MainThread
import com.laputa.zeej.std_0005_architecture.model.data.UserWithBooks
import com.laputa.zeej.std_0005_architecture.mvp.core.LoadingMVPView
import com.laputa.zeej.std_0005_architecture.mvp.core.MVPPresenter
import com.laputa.zeej.std_0005_architecture.mvp.core.MVPView

interface UserPresenter : MVPPresenter {
    fun loadUser(id: String)
}

interface UserView : MVPView,LoadingMVPView {


    @MainThread
    fun onUserLoad(user: UserWithBooks,time:Long)

    @MainThread
    fun onUserFail(msg: String)
}



