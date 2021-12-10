package com.negotium.remindword

import android.app.NotificationManager

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import androidx.core.app.NotificationCompat
import com.negotium.remindword.data.WordDataBase
import com.negotium.remindword.model.Word

const val  notificationID = 42
const val  CHANNEL_ID = "channel_id_notify_01"
const val titleExtra = "titleExtra"
const val  messageExtra = "messageExtra"
class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Build notification based on Intent
        val notification = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(getBodyMessage(context))
            .build()
        // Show notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }

    fun getBodyMessage(context: Context): String {
        val wordDb=WordDataBase.getDatabase(context)
        val wordDao = wordDb.wordDao()

        val readAllData: List<Word> = wordDao.getAll()
        var index = readAllData.size
        var random = readAllData.random()
        return random.englishWord + " = " + random.turkishWord
    }
}