package com.hidnapp.androidfinderplugin

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import kotlin.Throws

abstract class PluginService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    private val mBinder: IPluginInterface.Stub = object : IPluginInterface.Stub() {
        @Throws(RemoteException::class)
        override fun start(msg: String): Int {
            return run(msg)//defined in plugin
        }
    }

    //protected abstract double getResult(double number1, double number2);
    protected abstract fun run(msgContent: String?): Int
}