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
import dagger.hilt.android.AndroidEntryPoint
import projects.mayurmhm.mynotes.R
import projects.mayurmhm.mynotes.databinding.FragmentRegisterBinding
import projects.mayurmhm.mynotes.model.UserRequest
import projects.mayurmhm.mynotes.utils.NetworkResult
import projects.mayurmhm.mynotes.utils.TokenManager
import projects.mayurmhm.mynotes.viewModels.AuthViewModel
import javax.inject.Inject

/**
 * Register User
 */
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!! // made it null safe
    private val authViewModel by viewModels<AuthViewModel>() // view model object creation

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // if user already has a valid token
        if (tokenManager.getToken() != null) {
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // binding related code
        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {

                // raise API request to register user
                authViewModel.registerUser(getUserRequestObject())
            } else {
                // show error
                binding.txtError.text = validationResult.second
            }
        }

        binding.btnLogin.setOnClickListener {
            // navigate
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        //bind observers when view is created
        bindObservers()
    }

    // fetches data from screen & transforms as a User Request Object
    private fun getUserRequestObject(): UserRequest {
        val userName = binding.txtUsername.text.toString()
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(
            email = emailAddress,
            password = password,
            username = userName
        )
    }

    // check input with regex
    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequestObject()
        return authViewModel.validateCredentials(
            userRequest.username,
            userRequest.email,
            userRequest.password,
            false
        )
    }

    //bind observers when view is created
    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            // hide progress
            binding.progressBar.isVisible = false
            // Whenever live data changes this gets called
            when (it) {
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is NetworkResult.Success -> {
                    // save the token
                    tokenManager.saveToken(it.data!!.token)
                    // navigate to notes screen
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}