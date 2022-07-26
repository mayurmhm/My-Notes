package projects.mayurmhm.mynotes.viewModels

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import projects.mayurmhm.mynotes.model.UserRequest
import projects.mayurmhm.mynotes.model.UserResponse
import projects.mayurmhm.mynotes.repository.UserRepository
import projects.mayurmhm.mynotes.utils.NetworkResult
import javax.inject.Inject

// It will be accessible through the fragments whenever user interacts i.e button clicks
@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    // define live data variables which will be accessible from fragment
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = userRepository.userResponseLiveData

    // define operations
    // register user
    fun registerUser(userRequest: UserRequest) {
        // launch coroutine to do the job
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    // login user
    fun loginUser(userRequest: UserRequest) {
        // launch coroutine to do the job
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    // validate credentials entered by the user
    fun validateCredentials(
        userName: String,
        email: String,
        password: String,
        isLogin:Boolean
    ): Pair<Boolean, String> {
        var result = Pair(true, "")

        if ((!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            result = Pair(false, "Please provide credentials!")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result = Pair(false, "Please provide valid email!")
        } else if (password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5!")
        }

        return result
    }
}