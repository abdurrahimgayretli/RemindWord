package com.negotium.remindword.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.negotium.remindword.data.WordDataBase
import com.negotium.remindword.repository.WordRepository
import com.negotium.remindword.model.Word
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