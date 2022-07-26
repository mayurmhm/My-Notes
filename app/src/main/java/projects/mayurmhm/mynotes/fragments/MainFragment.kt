package projects.mayurmhm.mynotes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import projects.mayurmhm.mynotes.R
import projects.mayurmhm.mynotes.adapters.NoteAdapter
import projects.mayurmhm.mynotes.databinding.FragmentMainBinding
import projects.mayurmhm.mynotes.model.NoteResponse
import projects.mayurmhm.mynotes.utils.Constants.NOTE_RESPONSE_OBJECT
import projects.mayurmhm.mynotes.utils.NetworkResult
import projects.mayurmhm.mynotes.viewModels.NotesViewModel

/**
 * Main Fragments to show all notes
 * All Notes Fragment
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

    /**
     * todo mayur
     * 1. hide keyboards
     * 2. logout button will clear shared prefs
     * 3. splash rearrange
     * 4. room (response will be entity)
     * 5. paging
     * 6. flow
     * 7. Internet connectivity check
     * 8. apk sharing
     * 9. ad mobs
     * 10. pro guard
     * 11. SSO
     * 12. Swipe to refresh
     * 13. localization for buttons
     * 14. unit tests
     * 15. date time on notes
     * 16. notes backup file generation
     * 17. share notes
     * 18. safe args
     * 19. notification
     * 20. work mgr for sync
     * 21. ssl pinning
     * 22. flavours
     * 23. safe api calls
     * 24. use 2 repo : offline n online repo
     */

    // define bindings
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    // access view model
    private val notesViewModel by viewModels<NotesViewModel>()

    // adapter
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = NoteAdapter(::onNoteClicked)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bind observers
        bindObservers()

        // get notes request
        notesViewModel.getNotes()

        // set rv properties
        binding.noteList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // set adapters
        binding.noteList.adapter = adapter

        // floating button is clicked
        binding.addNote.setOnClickListener {
            // navigate
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
    }

    // bind all observers
    private fun bindObservers() {
        notesViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    // set adapter
                    adapter.submitList(it.data)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // when a note is clicked
    private fun onNoteClicked(noteResponse: NoteResponse) {
        // redirect selected note to the noteFragment with selected note as a bundle
        // by converting note response to a json string by the help of GSON
        val bundle = Bundle()
        bundle.putString(NOTE_RESPONSE_OBJECT, Gson().toJson(noteResponse))

        // navigate
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

}