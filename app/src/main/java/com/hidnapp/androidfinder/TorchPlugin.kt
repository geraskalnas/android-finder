package com.hidnapp.androidfinder

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.SystemClock.sleep
import com.hidnapp.androidfinderplugin.PluginService

class TorchPlugin : PluginService() {
    private val DELAY_MS = 10000

    private var flashLightStatus: Boolean = false
    private lateinit var ctx: Context

    override fun run(msgContent: String?):Int{
        ctx = getContext()

        openFlashLight()

        sleep(DELAY_MS.toLong())

        openFlashLight()


        return if (flashLightStatus) 1 else 0
    }

    private fun openFlashLight() {
        val cameraManager = ctx.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if (!flashLightStatus) {
            try {
                cameraManager.setTorchMode(cameraId, true)
                flashLightStatus = true

            } catch (e: CameraAccessException) {
            }
        } else {
            try {
                cameraManager.setTorchMode(cameraId, false)
                flashLightStatus = false
            } catch (e: CameraAccessException) {
            }
        }

    }
}
