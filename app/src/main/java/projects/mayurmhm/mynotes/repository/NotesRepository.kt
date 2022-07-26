package projects.mayurmhm.mynotes.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import projects.mayurmhm.mynotes.api.NotesAPI
import projects.mayurmhm.mynotes.model.NoteRequest
import projects.mayurmhm.mynotes.model.NoteResponse
import projects.mayurmhm.mynotes.utils.Constants
import projects.mayurmhm.mynotes.utils.Constants.TAG
import projects.mayurmhm.mynotes.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

// Class that talks with remote/local data source
class NotesRepository @Inject constructor(private val notesAPI: NotesAPI) {

    // mutable live data (Use this for changing the actual data)
    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    private val _noteStatusLiveData = MutableLiveData<NetworkResult<String>>()

    // public live data
    val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>> get() = _notesLiveData

    // will need status for acknowledgement of create/update/delete notes
    val noteStatusLiveData: LiveData<NetworkResult<String>> get() = _noteStatusLiveData

    // get notes
    suspend fun getNotes() {
        // start loader
        _notesLiveData.postValue(NetworkResult.Loading())

        // hit request
        val response = notesAPI.getNotes()
        Log.d(TAG, response.body().toString())

        // todo mayur create a generic class & shift this code along with UserRepository if else code
        handleResponse(response)
    }

    // create note
    suspend fun createNote(noteRequest: NoteRequest) {
        // start loader
        _noteStatusLiveData.postValue(NetworkResult.Loading())

        // hit api
        val response = notesAPI.createNote(noteRequest)
        Log.d(TAG, response.body().toString())

        // handle response with status message
        handleResponseWithStatus(response, "Note created!")
    }

    // update note
    suspend fun updateNote(noteId: String, noteRequest: NoteRequest) {
        // start loader
        _noteStatusLiveData.postValue(NetworkResult.Loading())

        // hit api
        val response = notesAPI.updateNote(noteId, noteRequest)
        Log.d(TAG, response.body().toString())

        // handle response with status message
        handleResponseWithStatus(response, "Note updated!")
    }

    // delete note
    suspend fun deleteNote(noteId: String) {
        // start loader
        _noteStatusLiveData.postValue(NetworkResult.Loading())

        // hit api
        val response = notesAPI.deleteNote(noteId)
        Log.d(TAG, response.body().toString())

        // handle response with status message
        handleResponseWithStatus(response, "Note deleted!")
    }

    // get notes response
    private fun handleResponse(response: Response<List<NoteResponse>>) {
        if (response.isSuccessful && response.body() != null) {
            // success
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            // extract error message from error body
            val errorObjectFromJson = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(
                NetworkResult.Error(
                    errorObjectFromJson.getString(
                        Constants.MESSAGE_KEY
                    )
                )
            )
        } else {
            // edge case error
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong!!"))
        }
    }

    // handle response with status
    private fun handleResponseWithStatus(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            // success
            _noteStatusLiveData.postValue(NetworkResult.Success(message))
        } else {
            // edge case error
            _noteStatusLiveData.postValue(NetworkResult.Error("Something went wrong!!"))
        }
    }

}