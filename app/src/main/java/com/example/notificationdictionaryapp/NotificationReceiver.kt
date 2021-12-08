package com.example.notificationdictionaryapp

import android.app.NotificationManager

import android.R
import android.app.Application

import android.content.Intent

import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.room.Query
import com.example.notificationdictionaryapp.data.WordDataBase
import com.example.notificationdictionaryapp.fragments.list.ListAdapter
import com.example.notificationdictionaryapp.fragments.list.ListFragment
import com.example.notificationdictionaryapp.model.Word
import com.example.notificationdictionaryapp.repository.WordRepository
import com.example.notificationdictionaryapp.viewmodel.WordViewModel
import com.google.android.gms.internal.phenotype.zzh.init
import kotlin.random.Random

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