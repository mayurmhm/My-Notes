package projects.mayurmhm.mynotes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import projects.mayurmhm.mynotes.databinding.FragmentNoteBinding
import projects.mayurmhm.mynotes.model.NoteRequest
import projects.mayurmhm.mynotes.model.NoteResponse
import projects.mayurmhm.mynotes.utils.Constants.NOTE_RESPONSE_OBJECT
import projects.mayurmhm.mynotes.utils.NetworkResult
import projects.mayurmhm.mynotes.viewModels.NotesViewModel


/**
 * Add/Edit Note Fragment
 */
@AndroidEntryPoint
class NoteFragment : Fragment() {

    // define binding variables & view model objects
    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private var note: NoteResponse? = null
    private val noteViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set initial data
        setInitialData()

        // bind handlers
        bindHandlers()

        // bind observers
        bindObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // check whether note data is present or not
    private fun setInitialData() {
        val jsonNote = arguments?.getString(NOTE_RESPONSE_OBJECT)

        if (jsonNote != null) {
            // existing note
            binding.btnSubmit.text = "Update"

            // get note
            note = Gson().fromJson(jsonNote, NoteResponse::class.java)

            // set data
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        } else {
            // new note
            binding.btnDelete.isVisible = false
            binding.addEditText.text = "Add Note"
            binding.btnSubmit.text = "Create"
        }
    }

    // binds handlers
    private fun bindHandlers() {
        // delete button action
        binding.btnDelete.setOnClickListener {
            note?.let {
                noteViewModel.deleteNote(it._id)
            }
        }

        // submit button action for update & create note
        binding.btnSubmit.setOnClickListener {
            // fetch data
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()

            // create note request object
            val noteRequest = NoteRequest(title = title, description = description)

            if (note == null) {
                // new note
                noteViewModel.createNote(noteRequest)
            } else {
                // update existing note
                noteViewModel.updateNote(noteId = note!!._id, noteRequest = noteRequest)
            }
        }
    }

    // binds observers
    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Error -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
            }
        })
    }

}