package projects.mayurmhm.mynotes.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import projects.mayurmhm.mynotes.model.NoteRequest
import projects.mayurmhm.mynotes.repository.NotesRepository
import javax.inject.Inject

// It will be accessible through the fragments whenever user interacts i.e button clicks
@HiltViewModel
class NotesViewModel @Inject constructor(private val notesRepository: NotesRepository) :
    ViewModel() {

    // define live data variables which will be accessible from fragment
    val notesLiveData get() = notesRepository.notesLiveData
    val statusLiveData get() = notesRepository.noteStatusLiveData

    // define all operations
    fun getNotes() {
        viewModelScope.launch {
            notesRepository.getNotes()
        }
    }

    fun createNote(noteRequest: NoteRequest) {
        viewModelScope.launch {
            notesRepository.createNote(noteRequest)
        }
    }

    fun updateNote(noteId: String, noteRequest: NoteRequest) {
        viewModelScope.launch {
            notesRepository.updateNote(noteId, noteRequest)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
        }
    }
}