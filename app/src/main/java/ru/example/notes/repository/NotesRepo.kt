package ru.example.notes.repository

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import ru.example.notes.data.NotesDao
import ru.example.notes.data.NotesDatabase
import ru.example.notes.model.Note

class NotesRepo(context: Context, scope: CoroutineScope) {
    val allNotes: LiveData<List<Note>>
    private val notesDao: NotesDao

    init {
        val database: NotesDatabase = NotesDatabase.getDatabase(context, scope)
        notesDao = database.notesDao()
        allNotes = notesDao.getAllNotes()
    }

    fun insert(note: Note) {
        notesDao.insert(note)
    }

    fun update(note: Note) {
        notesDao.update(note)
    }

    fun delete(id: Int) {
        notesDao.delete(id)
    }

    fun getNote(id: Int) : Note {
        return notesDao.getNote(id)
    }
}