package com.example.notificationdictionaryapp

import android.app.NotificationManager

import android.R

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.app.NotificationCompat

const val  notificationID = 42
const val  CHANNEL_ID = "channel_id_notify_01"
const val titleExtra = "titleExtra"
const val  messageExtra = "messageExtra"
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Build notification based on Intent
        val notification = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(com.example.notificationdictionaryapp.R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()
        // Show notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}