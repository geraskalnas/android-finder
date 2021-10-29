package com.hidnapp.androidfinder

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.hidnapp.androidfinder.databinding.ActivityMainBinding
import com.hidnapp.androidfinderplugin.IPluginInterface
import java.lang.Thread.sleep
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var ctx: Context

    private var pluginInterface: IPluginInterface? = null
    private var serviceConnectionAlive: Boolean = false
    private var number: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ctx = applicationContext

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
        //view -> {
            val ps = findPlugins()
            val p = ps!![0]
            bindPlugin(p)
        //}
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()*/
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun textMessage(s: String, c:Context, long: Boolean = false) {
        Toast.makeText(c, s, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()

    }

    fun findPlugins(): List<Plugin>? {
        val pluginServices = packageManager.queryIntentServices(
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

    @Throws(Exception::class)
    fun bindPlugin(plugin: Plugin?) {
        val bindIntent = Intent()
        bindIntent.setClassName(plugin!!.servicePackageName, plugin.serviceName)
        bindService(bindIntent, pluginServiceConnection, Context.BIND_AUTO_CREATE)
        serviceConnectionAlive = true


        val t = Thread {
            //while(serviceConnectionAlive) { }
            sleep(5000)

            try {
                unbindService(pluginServiceConnection)
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
                val r = pluginInterface?.start(number++.toString())
                textMessage("Return: " + r.toString(), ctx)
            } catch (e: RemoteException) {
                //result.setText("Error");
            }
            serviceConnectionAlive = false
        }

        override fun onServiceDisconnected(name: ComponentName) {
            textMessage("service killed", ctx)
            pluginInterface = null
            serviceConnectionAlive = false
        }
    }
}
