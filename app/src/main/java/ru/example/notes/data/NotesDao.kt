package ru.example.notes.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.example.notes.model.Note

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Query("DELETE FROM note_table WHERE id=:id")
    fun delete(id: Int)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>
}