package com.laputa.zeej.std_0006_android.binder.case02

import android.app.Service
import android.content.Intent
import android.os.IBinder

class UserService : Service() {

    override fun onBind(intent: Intent?): IBinder {
        return UserBinder()
    }
}


