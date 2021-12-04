package com.example.notificationdictionaryapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notificationdictionaryapp.data.WordDataBase
import com.example.notificationdictionaryapp.repository.WordRepository
import com.example.notificationdictionaryapp.model.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WordViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Word>>
    private val repository: WordRepository

    init{
        val wordDao = WordDataBase.getDatabase(application).wordDao()
        repository = WordRepository(wordDao)
        readAllData = repository.readAllData
    }

    fun addWord(word: Word){
        viewModelScope.launch (Dispatchers.IO){
            repository.addWord(word)
        }
    }

    fun updateWord(word: Word){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateWord(word)
        }
    }

    fun deleteWord(word: Word){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWord(word)
        }
    }

    fun deleteAllWords(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllWords()
        }
    }
}