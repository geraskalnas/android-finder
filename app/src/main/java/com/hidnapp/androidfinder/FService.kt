package com.hidnapp.androidfinder

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.Context
import android.util.Log

abstract class FService : Service() {
    private lateinit var ctx: Context

    override fun onCreate() {
        super.onCreate()
        ctx = getApplicationContext()

        Log.d("FService", "start")

        val ps = findPlugins(ctx)
        val p = ps?.get(0)
        bindPlugin(ctx, p)

        Log.d("FService", "stop")
        stopSelf()
    }
}