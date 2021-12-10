package com.negotium.remindword.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.negotium.remindword.model.Setting
import java.util.concurrent.Executors

@Database(entities = [Setting::class], version = 1,exportSchema = false)
abstract class SettingDataBase: RoomDatabase(){
    abstract fun settingDao(): SettingDao

    companion object {
        private var INSTANCE: SettingDataBase? = null

        fun getInstance(context: Context): SettingDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, SettingDataBase::class.java, "YourDatabase.db")
                .addCallback(seedDatabaseCallback(context))
                .allowMainThreadQueries()
                .build()

        private fun seedDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        var yourDao = getInstance(context)!!.settingDao()
                        val Setting = Setting(0,"Notification","False")
                        yourDao.insert(Setting)
                    }
                }
            }
        }
        private val IO_EXECUTOR = Executors.newSingleThreadExecutor()
        fun ioThread(f : () -> Unit) {
            IO_EXECUTOR.execute(f)
        }
    }
}