package com.hidnapp.androidfinder;

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.RemoteException
import com.hidnapp.androidfinderplugin.IPluginInterface
import java.lang.Thread.sleep
import java.util.ArrayList


    private var pluginInterface: IPluginInterface? = null
    private var serviceConnectionAlive: Boolean = false

    fun findPlugins(ctx: Context): List<Plugin>? {
        val pluginServices = ctx.packageManager.queryIntentServices(
                Intent("com.hidnapp.androidfinder.PLUGIN"),
                PackageManager.GET_META_DATA
        )
        val plugins: MutableList<Plugin> = ArrayList()
        for (pluginService in pluginServices) {
            val plugin = Plugin(pluginService!!)
            plugins.add(plugin)
        }
        if (plugins.size==0) return null
        return plugins
    }


    fun bindPlugin(ctx: Context, plugin: Plugin?) {
        val bindIntent = Intent()
        bindIntent.setClassName(plugin!!.servicePackageName, plugin.serviceName)
        ctx.bindService(bindIntent, pluginServiceConnection, Context.BIND_AUTO_CREATE)
        serviceConnectionAlive = true


        val t = Thread {
            //while(serviceConnectionAlive) { }
            sleep(5000)

            try {
                ctx.unbindService(pluginServiceConnection)
                //textMessage("Unbind executed successfully", ctx)
            }catch (e: Exception){
                //textMessage("Error while unbinding: $e", ctx, true)
            }
        }
        t.start()
    }

    private val pluginServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            pluginInterface = IPluginInterface.Stub.asInterface(service)

            try {
                //val r = pluginInterface?.start(number++.toString())
                val r = pluginInterface?.start("")
                //textMessage("Return: " + r.toString(), ctx)
            } catch (e: RemoteException) {
                //result.setText("Error");
            }
            serviceConnectionAlive = false
        }

        override fun onServiceDisconnected(name: ComponentName) {
            //textMessage("service killed", ctx)
            pluginInterface = null
            serviceConnectionAlive = false
        }
    }
