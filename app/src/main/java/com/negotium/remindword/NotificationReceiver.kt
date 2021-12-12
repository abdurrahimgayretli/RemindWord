package com.negotium.remindword

import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.jwang123.flagkit.FlagKit
import com.negotium.remindword.data.WordDataBase
import com.negotium.remindword.model.Word
import com.negotium.remindword.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.fragment_update.view.*

const val  notificationID = 42
const val  CHANNEL_ID = "channel_id_notify_01"
const val titleExtra = "titleExtra"
class NotificationReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val intent = Intent(context, MainActivity::class.java)
        val pending = PendingIntent.getActivity(context, notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // Build notification based on Intent
        val notification = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(getBodyMessage(context))
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()
        // Show notification
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }



    fun getBodyMessage(context: Context): String {
        val wordDb=WordDataBase.getDatabase(context)
        val wordDao = wordDb.wordDao()
        val readAllData: List<Word> = wordDao.getAll()
        val random = readAllData.random()

        return random.fromWord + " " + flagIcon(random.fromFlag) + " = " + random.toWord + " " + flagIcon(random.toFlag)

    }
    fun flagIcon(string:String): String{
        return when(string){
            "tr" -> "\uD83C\uDDF9\uD83C\uDDF7"
            "ru" -> "\uD83C\uDDF7\uD83C\uDDFA"
            "fr" -> "\uD83C\uDDEB\uD83C\uDDF7"
            "it" -> "\uD83C\uDDEE\uD83C\uDDF9"
            "de" -> "\uD83C\uDDE9\uD83C\uDDEA"
            "es" -> "\uD83C\uDDEA\uD83C\uDDF8"
            else -> "\uD83C\uDDEC\uD83C\uDDE7"
        }
    }

}