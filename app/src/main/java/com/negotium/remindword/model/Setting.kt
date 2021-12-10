package com.negotium.remindword.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "setting_table",indices = arrayOf(Index(value = arrayOf("key"), unique = true)))
data class Setting (
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name="key")
    val key: String,

    val value: String
): Parcelable