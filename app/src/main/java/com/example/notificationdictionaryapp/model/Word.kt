package com.example.notificationdictionaryapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "word_table" )
data class Word (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val englishWord: String,
    val turkishWord: String
): Parcelable