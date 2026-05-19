package com.example.tugaspam3.platform

import android.content.Context
import android.os.Build

class AndroidDeviceInfo(private val context: Context) : DeviceInfo {
    override val model: String = Build.MODEL
    override val manufacturer: String = Build.MANUFACTURER
    override val osVersion: String = Build.VERSION.RELEASE
    override val apiLevel: Int = Build.VERSION.SDK_INT
}
