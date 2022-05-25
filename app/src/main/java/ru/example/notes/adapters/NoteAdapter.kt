package ru.example.notes.adapters

import android.annotation.SuppressLint
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_item.view.*
import ru.example.notes.R
import ru.example.notes.model.Note


class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private val notes: ArrayList<Note> = ArrayList()
    private var listener: OnNoteClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val noteView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(noteView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int = notes.size

    fun setNotes(notes: List<Note>) {
        val diffUtil = NotesDiffUtil(this.notes, notes)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        this.notes.clear()
        this.notes.addAll(notes)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getNoteAt(position: Int): Note {
        return notes[position]
    }

    inner class NoteViewHolder(noteView: View): RecyclerView.ViewHolder(noteView) {
        private val noteDate: TextView = noteView.dateOfNote
        private val title: TextView = noteView.noteTitle
        private val noteText: TextView = noteView.noteText

        init {
            itemView.setOnClickListener {
                listener?.let { listener ->
                    val position: Int = bindingAdapterPosition
                    listener.onNoteClick(notes[position], itemView)
                }
            }

            itemView.setOnLongClickListener {
                listener?.let { listener ->
                    val position: Int = bindingAdapterPosition
                    listener.onNoteLongClick(notes[position], itemView)
                }
                true
            }
        }

        fun bind(note: Note) {
            title.text = note.title
            noteText.text = note.noteText
            noteDate.text = note.date
            when(note.selected) {
                true -> itemView.setBackgroundResource(R.drawable.selector)
                false -> itemView.setBackgroundResource(R.drawable.note_shape)
            }
        }
    }

    fun setOnNoteClickListener(listener: OnNoteClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataChanged() {
        notifyDataSetChanged()
    }

    interface OnNoteClickListener {
        fun onNoteClick(note: Note?, itemView: View)
        fun onNoteLongClick(note: Note, itemView: View)
    }
}