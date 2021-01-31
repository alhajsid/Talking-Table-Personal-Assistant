package com.example.demospeechrecognization

import android.content.Intent
import android.util.Log
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context

var volumePrev = 0

private val broadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        if ("android.media.VOLUME_CHANGED_ACTION" == intent.action) {

            val volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0)

            Log.i(TAG, "volume = $volume")

            if (volumePrev < volume) {
                Log.e(TAG, "You have pressed volume up button")
            } else {
                Log.e(TAG, "You have pressed volume down button")
            }
            volumePrev = volume
        }
    }
}



