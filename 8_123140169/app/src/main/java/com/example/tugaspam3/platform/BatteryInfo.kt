package com.example.tugaspam3.platform

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

/**
 * Interface for Battery Information platform feature.
 * Following the expect/actual pattern structure for Android.
 */
interface BatteryInfo {
    val level: Int
    val isCharging: Boolean
}

class AndroidBatteryInfo(private val context: Context) : BatteryInfo {
    private val batteryStatus: Intent? by lazy {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(null, ifilter)
    }

    override val level: Int
        get() {
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            return (level * 100 / scale.toFloat()).toInt()
        }

    override val isCharging: Boolean
        get() {
            val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
        }
}
