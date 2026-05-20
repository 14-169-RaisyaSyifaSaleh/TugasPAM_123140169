package com.example.tugaspam3.platform

interface DeviceInfo {
    val model: String
    val osVersion: String
    val manufacturer: String
}

class AndroidDeviceInfo : DeviceInfo {
    override val model: String = android.os.Build.MODEL
    override val osVersion: String = android.os.Build.VERSION.RELEASE
    override val manufacturer: String = android.os.Build.MANUFACTURER
}
