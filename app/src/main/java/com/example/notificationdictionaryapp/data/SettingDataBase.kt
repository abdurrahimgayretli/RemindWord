package com.example.notificationdictionaryapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notificationdictionaryapp.model.Setting

@Database(entities = [Setting::class], version = 1,exportSchema = false)
abstract class SettingDataBase: RoomDatabase(){
    abstract fun settingDao(): SettingDao
    companion object{
        @Volatile
        private var INSTANCE: SettingDataBase? = null

        fun getDatabase(context: Context): SettingDataBase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SettingDataBase::class.java,
                    "setting_database"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}