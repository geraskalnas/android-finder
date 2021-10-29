package com.hidnapp.androidfinder

import android.os.SystemClock.sleep
import com.hidnapp.androidfinderplugin.PluginService

class TorchPlugin : PluginService() {
    override fun run(msgContent: String?):Int{
        if(msgContent=="on"){
            sleep(2000)
        }else{

        }

        get

        if (msgContent==null) return -1
        return msgContent.toInt()
    }
}
