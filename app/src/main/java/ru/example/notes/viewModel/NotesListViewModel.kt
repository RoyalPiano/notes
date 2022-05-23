package ru.example.notes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.example.notes.model.Note
import ru.example.notes.repository.NotesRepo

class NotesListViewModel(application: Application): AndroidViewModel(application) {
    private val repo = NotesRepo(application.applicationContext, viewModelScope)
    val allNotes = repo.allNotes
    var isSelectedAny = false
    val selectedNotes = mutableListOf<Int>()

    private fun clearSelected() {
        isSelectedAny = false
        selectedNotes.clear()
    }

    fun deleteSelectedNotes() {
        for (id in selectedNotes) {
            delete(id)
        }
        clearSelected()
    }

    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(note)
    }

    fun delete(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repo.delete(id)
    }

    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repo.update(note)
    }
}