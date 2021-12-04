package com.example.notificationdictionaryapp.repository

import androidx.lifecycle.LiveData
import com.example.notificationdictionaryapp.data.WordDao
import com.example.notificationdictionaryapp.model.Word

class WordRepository(private val wordDao: WordDao) {

    val readAllData: LiveData<List<Word>> = wordDao.readAllData()

    suspend fun addWord(word: Word){
        wordDao.addWord(word)
    }

    suspend fun updateWord(word:Word){
        wordDao.updateWord(word)
    }

    suspend fun deleteWord(word: Word){
        wordDao.deleteWord(word)
    }
    suspend fun deleteAllWords(){
        wordDao.deleteAllWords()
    }
}