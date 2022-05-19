package ru.example.notes

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.example.notes.adapters.NoteAdapter
import ru.example.notes.databinding.FragmentNotesListBinding
import ru.example.notes.model.Note
import ru.example.notes.viewModel.NotesListViewModel

class NotesListFragment: Fragment() {

    private var mainMenu: Menu? = null
    private val notesListViewModel: NotesListViewModel by viewModels()
    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!
    val noteAdapter = NoteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        noteAdapter.setOnNoteClickListener(object :
        NoteAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note?, itemView: View) {
                if(note != null)
                {
                    if(noteAdapter.isSelectedAny)
                    {
                        if(note.selected) {
                            deselectItem(note, itemView)
                        }
                        else {
                            selectItem(note, itemView)
                        }
                    }
                    else {
                        val bundle = Bundle()
                        bundle.putInt("noteId", note.id)
                        bundle.putString("noteTitle", note.title)
                        bundle.putString("noteText", note.noteText)
                        Navigation.findNavController(binding.root)
                            .navigate(R.id.action_navigation_home_to_navigation_edit_note, bundle)
                    }
                }
            }

            override fun onNoteLongClick(note: Note, itemView: View) {
                if(!noteAdapter.isSelectedAny) {
                    setDeleteMenuVisibility(true)
                    markFirstSelectedItem(note, itemView)
                }
            }
        })

        notesListViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.setNotes(notes)
        }

        binding.notesList.layoutManager = StaggeredGridLayoutManager(2, 1) //GridLayoutManager(requireContext(), 2)
        binding.notesList.adapter = noteAdapter

        binding.addNoteBtn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_navigation_edit_note)
        }

        return binding.root
    }

    fun markFirstSelectedItem(note: Note, itemView: View): Boolean {
        markItem(itemView)
        noteAdapter.selectedNotes.add(note.id)
        noteAdapter.isSelectedAny = true
        note.selected = true

        return true
    }

    private fun deselectItem(note: Note, itemView: View): Boolean {
        unMarkItem(itemView)
        note.selected = false
        noteAdapter.selectedNotes.remove(note.id)
        if(noteAdapter.selectedNotes.isEmpty()) {
            noteAdapter.isSelectedAny = false
            setDeleteMenuVisibility(false)
        }
        return true
    }

    private fun unMarkItem(itemView: View) {
        itemView.setBackgroundResource(R.drawable.note_shape)
        itemView.setPadding(0)
    }

    private fun markItem(itemView: View) {
        itemView.setBackgroundResource(R.drawable.selector)
    }

    private fun selectItem(note: Note, itemView: View): Boolean {
        markItem(itemView)
        note.selected = true
        noteAdapter.selectedNotes.add(note.id)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_menu) {
            val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
            bottomSheet.setContentView(R.layout.dialog_delete)
            bottomSheet.show()
            val textViewYes = bottomSheet.findViewById<TextView>(R.id.dialog_yes)
            val textViewNo = bottomSheet.findViewById<TextView>(R.id.dialog_no)

            textViewYes?.setOnClickListener {
                deleteSelectedNotes()
                setDeleteMenuVisibility(false)
                bottomSheet.dismiss()
            }

            textViewNo?.setOnClickListener {
                bottomSheet.dismiss()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteSelectedNotes() {
        for(id in noteAdapter.selectedNotes) {
            notesListViewModel.delete(id)
        }
        noteAdapter.clearSelectedNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        mainMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun setDeleteMenuVisibility(isVisible: Boolean) {
        mainMenu?.findItem(R.id.delete_menu)?.isVisible = isVisible
    }
}