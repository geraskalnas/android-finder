package com.hidnapp.androidfinder

import android.content.Context
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import com.hidnapp.androidfinderplugin.PluginService
import java.lang.Thread.sleep

class RingPlugin : PluginService() {
    private val DELAY_MS = 5000

    override fun run(msgContent: String?):Int{
        val ctx: Context = getContext()

        val alert: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val r = RingtoneManager.getRingtone(ctx, alert)//.setStreamType(AudioManager.STREAM_ALARM)

        val am = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING),0)

        r.play()
        sleep(DELAY_MS.toLong())
        r.stop()

        return 0
    }
}