package ru.example.notes.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "note_table")
@Parcelize
data class Note(
    val title: String,
    val noteText: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var selected: Boolean = false,
    ): Parcelable
