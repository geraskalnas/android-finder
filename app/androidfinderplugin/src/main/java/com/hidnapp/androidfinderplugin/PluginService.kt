package com.hidnapp.androidfinderplugin

import android.app.Service
import android.content.Context
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
        override fun start(msg: String): Int {//access plugin's function run
            return run(msg)
        }
    }

    protected abstract fun run(msgContent: String?): Int//defined in plugin
    
    protected fun getContext(): Context {
        return applicationContext;
    }
}
