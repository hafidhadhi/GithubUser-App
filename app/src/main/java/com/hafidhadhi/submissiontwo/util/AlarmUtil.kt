package com.hafidhadhi.submissiontwo.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hafidhadhi.submissiontwo.BuildConfig
import com.hafidhadhi.submissiontwo.receiver.AlarmReceiver
import java.util.*
import java.util.concurrent.TimeUnit

const val ACTION_START_REMINDER =
    "${BuildConfig.APPLICATION_ID}.action.START_REMINDER"

const val START_REMINDER_REQUEST_CODE = 100

fun buildPendingIntent(
    context: Context,
    intentAction: String,
    alarmRequestCode: Int,
    receiver: Class<*>
): PendingIntent {
    val notifyIntent = Intent(context, receiver).apply {
        action = intentAction
    }
    return PendingIntent.getBroadcast(
        context,
        alarmRequestCode,
        notifyIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}

fun isAlarmUp(
    context: Context,
    intentAction: String,
    alarmRequestCode: Int,
    receiver: Class<*>
): Boolean {
    val notifyIntent = Intent(context, receiver).apply {
        action = intentAction
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        alarmRequestCode,
        notifyIntent,
        PendingIntent.FLAG_NO_CREATE
    )
    return pendingIntent != null
}

fun AlarmManager.triggerReminder(
    context: Context,
    enabled: Boolean
): Long? {
    val pendingIntent = buildPendingIntent(
        context,
        ACTION_START_REMINDER,
        START_REMINDER_REQUEST_CODE,
        AlarmReceiver::class.java
    )
    var scheduledTime = System.currentTimeMillis()
    if (enabled) {
        val currentTime = Calendar.getInstance()
        val triggerTime = Calendar.getInstance().apply {
            val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            if (currentHour > 9) add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
        scheduledTime = triggerTime
        val interval = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            interval,
            pendingIntent
        )
    } else {
        cancel(pendingIntent)
        pendingIntent.cancel()
    }
    val isAlarmUp = isAlarmUp(
        context,
        ACTION_START_REMINDER,
        START_REMINDER_REQUEST_CODE,
        AlarmReceiver::class.java
    )
    return if (isAlarmUp) scheduledTime else null
}