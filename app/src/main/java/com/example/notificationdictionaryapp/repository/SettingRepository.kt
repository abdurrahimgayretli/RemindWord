package com.example.notificationdictionaryapp.repository

import androidx.lifecycle.LiveData
import com.example.notificationdictionaryapp.data.SettingDao
import com.example.notificationdictionaryapp.model.Setting

class SettingRepository(private val settingDao: SettingDao) {

    val readAllData: LiveData<List<Setting>> = settingDao.readAllData()

    suspend fun addSetting(setting: Setting){
        settingDao.addSetting(setting)
    }

    suspend fun updateSetting(setting: Setting){
        settingDao.updateSetting(setting)
    }

    suspend fun deleteSetting(setting: Setting){
        settingDao.deleteSetting(setting)
    }
    suspend fun deleteAllSettings(){
        settingDao.deleteAllSettings()
    }
}