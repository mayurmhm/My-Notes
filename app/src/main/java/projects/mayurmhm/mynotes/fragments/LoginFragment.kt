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
import projects.mayurmhm.mynotes.databinding.FragmentLoginBinding
import projects.mayurmhm.mynotes.model.UserRequest
import projects.mayurmhm.mynotes.utils.Constants.EMPTY_STRING
import projects.mayurmhm.mynotes.utils.NetworkResult
import projects.mayurmhm.mynotes.utils.TokenManager
import projects.mayurmhm.mynotes.viewModels.AuthViewModel
import javax.inject.Inject

/**
 * Login Fragment
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    // initialize variables
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // inflate the view
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.loginUser(getUserRequestObject())
            } else {
                binding.txtError.text = validationResult.second
            }

        }

        binding.btnSignUp.setOnClickListener {
            // send user back to the register fragment
            findNavController().popBackStack()
        }

        bindObservers()
    }

    // check input with regex
    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequestObject()
        return authViewModel.validateCredentials(
            userRequest.username,
            userRequest.email,
            userRequest.password,
            true
        )
    }

    // fetches data from screen & transforms as a User Request Object
    private fun getUserRequestObject(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return UserRequest(
            email = emailAddress,
            password = password,
            username = EMPTY_STRING
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

                    // navigate
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}