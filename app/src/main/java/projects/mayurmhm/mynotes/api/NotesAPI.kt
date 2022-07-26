package projects.mayurmhm.mynotes.api

import projects.mayurmhm.mynotes.model.NoteRequest
import projects.mayurmhm.mynotes.model.NoteResponse
import retrofit2.Response
import retrofit2.http.*

// contains notes related authenticated endpoints
interface NotesAPI {

    // get all notes
    @GET("/notes")
    suspend fun getNotes(): Response<List<NoteResponse>>

    // create a new note
    @POST("/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    // update a note
    @PUT("/notes/{noteId}")
    suspend fun updateNote(
        @Path("noteId") noteId: String,
        @Body noteRequest: NoteRequest
    ): Response<NoteResponse>

    // delete a note
    @DELETE("/notes/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String): Response<NoteResponse>
}