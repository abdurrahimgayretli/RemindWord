package com.negotium.remindword.repository

import androidx.lifecycle.LiveData
import com.negotium.remindword.data.SettingDao
import com.negotium.remindword.model.Setting

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