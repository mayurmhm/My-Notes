package projects.mayurmhm.mynotes.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.json.JSONObject
import projects.mayurmhm.mynotes.api.UserAPI
import projects.mayurmhm.mynotes.model.UserRequest
import projects.mayurmhm.mynotes.model.UserResponse
import projects.mayurmhm.mynotes.utils.Constants.MESSAGE_KEY
import projects.mayurmhm.mynotes.utils.Constants.TAG
import projects.mayurmhm.mynotes.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

// Class that talks with remote/local data source
class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    // mutable live data (Use this for changing the actual data)
    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()

    // public live data
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        // start loader
        _userResponseLiveData.postValue(NetworkResult.Loading())

        // hit request
        val response = userAPI.signUp(userRequest)
        Log.d(TAG, response.body().toString())
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        // start loader
        _userResponseLiveData.postValue(NetworkResult.Loading())

        // hit request
        val response = userAPI.signIn(userRequest)
        Log.d(TAG, response.body().toString())
        handleResponse(response)
    }

    // handles response
    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            // success
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            // extract error message from error body
            val errorObjectFromJson = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(
                NetworkResult.Error(
                    errorObjectFromJson.getString(
                        MESSAGE_KEY
                    )
                )
            )
        } else {
            // edge case error
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong!!"))
        }
    }
}