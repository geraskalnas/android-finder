package com.hidnapp.androidfinder

import android.content.pm.ResolveInfo

class Plugin(resolveInfo: ResolveInfo) {
    val serviceName: String
    val servicePackageName: String
    val pattern: String
    val apiLevel: Int

    init {
        serviceName = resolveInfo.serviceInfo.name
        servicePackageName = resolveInfo.serviceInfo.packageName
        val metaData = resolveInfo.serviceInfo.metaData
        pattern =
            metaData.getString("com.hidnapp.androidfinder.META_DATA_PATTERN", "none")
        apiLevel = metaData.getInt("com.hidnapp.androidfinder.META_DATA_API_LEVEL", 0)
    }
}
