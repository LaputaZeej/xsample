package com.laputa.zeej.flow

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine

object LocationDataSource {
    @SuppressLint("MissingPermission")
    fun locationFlow(context: Context): Flow<String> = callbackFlow {
        val callback = LocationListener {
            try{
                Log.e("locations",it.toString())
                trySendBlocking(it.display)
                Thread{
                    while (true){
                        Thread.sleep(1000)
                        //offer("-${System.currentTimeMillis()}")
                        trySend("-${System.currentTimeMillis()})")
                    }
                }.start()
            }catch (e:Throwable){
                e.printStackTrace()
                close(e)
            }
        }
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
            1000, 10f, callback)
        awaitClose {
            i("[LocationDataSource]awaitClose!!!")
            manager.removeUpdates(callback)
        }
    }
}

val Location.display:String
    get() = "${this.latitude},${this.longitude}"


fun locationFlow(context: Context): LiveData<String> = liveData<String> {
    try {
        while (true){
            delay(1000)
            this.emit("livedata-${System.currentTimeMillis()}")
        }
    }catch (e:Throwable){
        emit("")
    }
}
