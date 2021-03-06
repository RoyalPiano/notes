package ru.example.notes

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.example.notes.adapters.NoteAdapter
import ru.example.notes.databinding.FragmentNotesListBinding
import ru.example.notes.model.Note
import ru.example.notes.viewModel.NotesListViewModel


class NotesListFragment: Fragment() {

    private var mainMenu: Menu? = null
    private val notesListViewModel: NotesListViewModel by activityViewModels()
    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!
    private val noteAdapter = NoteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        notesListViewModel.noteToEdit = null

        noteAdapter.setOnNoteClickListener(object :
        NoteAdapter.OnNoteClickListener {
            override fun onNoteClick(note: Note?, itemView: View) {
                if(note != null)
                {
                    if(notesListViewModel.isSelectedAny)
                    {
                        if(note.selected) {
                            deselectItem(note, itemView)
                        }
                        else {
                            selectItem(note, itemView)
                        }
                    }
                    else {
                        notesListViewModel.noteToEdit = note
                        val navController = findNavController()
                        notesListViewModel.clearSelectedNotes()
                        navController.navigate(R.id.action_navigation_home_to_navigation_edit_note)
                    }
                }
            }

            override fun onNoteLongClick(note: Note, itemView: View) {
                if(!notesListViewModel.isSelectedAny) {
                    setDeleteMenuVisibility(true)
                    markFirstSelectedItem(note, itemView)
                }
            }
        })

        notesListViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.setNotes(notes)
        }

        binding.notesList.layoutManager = StaggeredGridLayoutManager(2, 1)
        binding.notesList.adapter = noteAdapter

        binding.addNoteBtn.setOnClickListener {
            notesListViewModel.clearSelectedNotes()
            Navigation.findNavController(it).navigate(R.id.action_navigation_home_to_navigation_edit_note)
        }

        return binding.root
    }

    private fun markFirstSelectedItem(note: Note, itemView: View): Boolean {
        markItem(itemView)
        notesListViewModel.selectedNotes.add(note.id)
        notesListViewModel.isSelectedAny = true
        note.selected = true

        return true
    }

    private fun deselectItem(note: Note, itemView: View): Boolean {
        unMarkItem(itemView)
        note.selected = false
        notesListViewModel.selectedNotes.remove(note.id)
        if(notesListViewModel.selectedNotes.isEmpty()) {
            notesListViewModel.isSelectedAny = false
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
        notesListViewModel.selectedNotes.add(note.id)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_menu) {
            setDeleteMenuVisibility(false)
            val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
            bottomSheet.setContentView(R.layout.dialog_delete)
            bottomSheet.setOnCancelListener {
                setDeleteMenuVisibility(true)
            }
            bottomSheet.show()
            val textViewYes = bottomSheet.findViewById<TextView>(R.id.dialog_yes)
            val textViewNo = bottomSheet.findViewById<TextView>(R.id.dialog_no)

            textViewYes?.setOnClickListener {
                notesListViewModel.deleteSelectedNotes()
                bottomSheet.dismiss()
            }

            textViewNo?.setOnClickListener {
                bottomSheet.dismiss()
                setDeleteMenuVisibility(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        mainMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
        if (notesListViewModel.isSelectedAny)
            setDeleteMenuVisibility(true)
    }

    private fun setDeleteMenuVisibility(isVisible: Boolean) {
        mainMenu?.findItem(R.id.delete_menu)?.isVisible = isVisible
    }
}