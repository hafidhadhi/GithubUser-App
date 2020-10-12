package com.hafidhadhi.submissiontwo.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.CallSuper
import com.hafidhadhi.submissiontwo.util.ACTION_START_REMINDER
import com.hafidhadhi.submissiontwo.util.remindThem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(this::class.simpleName, intent.action.toString())
        when (intent.action) {
            ACTION_START_REMINDER -> {
                notificationManager.remindThem(context)
            }
            else -> {
            }
        }
    }
}

abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {
    }
}
