package ru.example.notes.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.example.notes.model.Note

class NotesDiffUtil (private val oldList: List<Note>,
                     private val newList: List<Note>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldNotePosition: Int, newNotePosition: Int): Boolean {
        val oldNote = oldList[oldNotePosition]
        val newNote = newList[newNotePosition]

        return oldNote.id == newNote.id
        //return  oldNotePosition == newNotePosition
    }

    override fun areContentsTheSame(oldNotePosition: Int, newNotePosition: Int): Boolean {
        val oldNote = oldList[oldNotePosition]
        val newNote = newList[newNotePosition]
        return oldNote == newNote
    }
}