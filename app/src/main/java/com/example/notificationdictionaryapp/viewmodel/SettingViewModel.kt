package com.example.notificationdictionaryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notificationdictionaryapp.data.SettingDataBase
import com.example.notificationdictionaryapp.model.Setting
import com.example.notificationdictionaryapp.repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Setting>>
    private val repository: SettingRepository

    init{
        val settingDao = SettingDataBase.getDatabase(application).settingDao()
        repository = SettingRepository(settingDao)
        readAllData = repository.readAllData
    }

    fun addSetting(setting: Setting){
        viewModelScope.launch (Dispatchers.IO){
            repository.addSetting(setting)
        }
    }

    fun updateSetting(setting: Setting){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateSetting(setting)
        }
    }

    fun deleteSetting(setting: Setting){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSetting(setting)
        }
    }

    fun deleteAllSettings(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllSettings()
        }
    }
}