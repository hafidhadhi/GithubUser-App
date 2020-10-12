package com.hafidhadhi.submissiontwo.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hafidhadhi.submissiontwo.BuildConfig
import com.hafidhadhi.submissiontwo.MainActivity
import com.hafidhadhi.submissiontwo.R

const val ACTION_NOTIFICATION_CLICK =
    "${BuildConfig.APPLICATION_ID}.action.ACTION_NOTIFICATION_CLICK"

const val REMINDER_NOTIF_ID = 200
const val ACTION_OPEN_MAIN_REQUEST_CODE = 201


fun NotificationManager.createChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            context.getString(R.string.notification_channel_id),
            context.getString(R.string.notification_channel_title),
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.remindThem(
    context: Context
) {

    val contentIntent = Intent(context, MainActivity::class.java).apply {
        action = ACTION_NOTIFICATION_CLICK
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
    }
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        ACTION_OPEN_MAIN_REQUEST_CODE,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.notification_channel_id)
    )

        .setSmallIcon(R.drawable.ic_twotone_access_alarm_24)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(context.getString(R.string.reminder_notif_content))
        .setAutoCancel(true)
        .setContentIntent(contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
    notify(REMINDER_NOTIF_ID, builder.build())
}