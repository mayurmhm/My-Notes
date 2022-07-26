package projects.mayurmhm.mynotes.api

import projects.mayurmhm.mynotes.model.UserRequest
import projects.mayurmhm.mynotes.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Contains User related endpoints
interface UserAPI {
    // sign up
    @POST("/users/signup")
    suspend fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    // sign in
    @POST("/users/signin")
    suspend fun signIn(@Body userRequest: UserRequest): Response<UserResponse>
}