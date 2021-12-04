package com.example.notificationdictionaryapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notificationdictionaryapp.model.Word

@Database(entities = [Word::class], version = 1,exportSchema = false)
abstract class WordDataBase: RoomDatabase(){
    abstract fun wordDao(): WordDao
    companion object{
        @Volatile
        private var INSTANCE: WordDataBase? = null

        fun getDatabase(context: Context): WordDataBase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordDataBase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
