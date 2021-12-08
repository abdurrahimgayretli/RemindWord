package com.example.notificationdictionaryapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notificationdictionaryapp.model.Word

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: Word)

    @Update
    suspend fun updateWord(word: Word)

    @Delete
    suspend fun deleteWord(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAllWords()

    @Query("SELECT * FROM word_table ORDER BY id ASC")
    fun  readAllData(): LiveData<List<Word>>

    @Query("SELECT * FROM word_table ORDER BY id ASC")
    fun  getAll(): List<Word>
}