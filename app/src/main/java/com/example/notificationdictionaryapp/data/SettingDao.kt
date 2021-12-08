package com.example.notificationdictionaryapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notificationdictionaryapp.model.Setting

@Dao
interface SettingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSetting(setting: Setting)

    @Update
    suspend fun updateSetting(setting: Setting)

    @Delete
    suspend fun deleteSetting(setting: Setting)

    @Query("DELETE FROM setting_table")
    suspend fun deleteAllSettings()

    @Query("SELECT * FROM setting_table ORDER BY id ASC")
    fun  readAllData(): LiveData<List<Setting>>

    @Query("SELECT * FROM setting_table ORDER BY id ASC")
    fun  getAll(): List<Setting>

    @Query("SELECT * FROM setting_table WHERE `key` = :key")
    fun  findByKey(key:String): Setting

    @Query("UPDATE setting_table SET value = :value WHERE `key` = :key")
    fun updateByKey(key:String,value:String)
}