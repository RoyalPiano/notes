package ru.example.notes

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.example.notes.databinding.FragmentEditNoteBinding
import ru.example.notes.model.Note
import ru.example.notes.viewModel.NotesListViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class EditNoteFragment: Fragment(R.layout.fragment_edit_note) {

    private val notesListViewModel: NotesListViewModel by viewModels()
    private var _binding: FragmentEditNoteBinding? = null
    private var mainMenu: Menu? = null
    private val binding get() = _binding!!
    private var isCreateView = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        isCreateView = arguments?.get("noteTitle") == null
        if(!isCreateView) {
            binding.noteTitleInput.setText(arguments?.get("noteTitle").toString())
            binding.noteTextInput.setText(arguments?.get("noteText").toString())
        }

        binding.saveNoteBtn.setOnClickListener {
            val currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            if(isCreateView) {
                val note = Note(binding.noteTitleInput.text.toString(),
                    binding.noteTextInput.text.toString(),
                    currentDate
                )
                notesListViewModel.insert(note)
            }
            else {
                val note = Note(binding.noteTitleInput.text.toString(),
                    binding.noteTextInput.text.toString(),
                    currentDate,
                    arguments?.getInt("noteId")!!
                )
                notesListViewModel.update(note)
            }

            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        mainMenu = menu
        mainMenu?.findItem(R.id.delete_menu)?.isVisible = !isCreateView
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_menu) {
            changeMenuVisibility(false)
            val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetStyle)
            bottomSheet.setContentView(R.layout.dialog_delete)
            bottomSheet.setOnCancelListener {
                changeMenuVisibility(true)
            }
            bottomSheet.show()
            val textViewYes = bottomSheet.findViewById<TextView>(R.id.dialog_yes)
            val textViewNo = bottomSheet.findViewById<TextView>(R.id.dialog_no)

            textViewYes?.setOnClickListener {
                notesListViewModel.delete(arguments?.getInt("noteId")!!)
                bottomSheet.dismiss()
                findNavController().navigateUp()
            }

            textViewNo?.setOnClickListener {
                changeMenuVisibility(true)
                bottomSheet.dismiss()
            }
        }

        if(item.itemId == android.R.id.home) {
            Navigation.findNavController(binding.root).navigate(R.id.action_navigation_edit_note_to_navigation_home)
        }

        return super.onOptionsItemSelected(item)
    }


    private fun changeMenuVisibility(flag: Boolean) {
        mainMenu?.findItem(R.id.delete_menu)?.isVisible = flag
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}