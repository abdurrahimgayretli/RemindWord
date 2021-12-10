package com.negotium.remindword.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.negotium.remindword.data.SettingDataBase
import com.negotium.remindword.model.Setting
import com.negotium.remindword.repository.SettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Setting>>
    private val repository: SettingRepository

    init{
        val settingDao = SettingDataBase.getInstance(application).settingDao()
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