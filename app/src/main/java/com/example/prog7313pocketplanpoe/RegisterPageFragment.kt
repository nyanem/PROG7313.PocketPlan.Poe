package com.example.prog7313pocketplanpoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.prog7313pocketplanpoe.R


class RegisterPageFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var numberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Link UI components
        nameEditText = view.findViewById(R.id.name_txtbox)
        surnameEditText = view.findViewById(R.id.surname_txtbox)
        numberEditText = view.findViewById(R.id.number_txtbox)
        emailEditText = view.findViewById(R.id.email_txtbox)
        usernameEditText = view.findViewById(R.id.usernameInput)
        passwordEditText = view.findViewById(R.id.passwordInput)
        confirmPasswordEditText = view.findViewById(R.id.confirmPassword_txtbox)
        registerButton = view.findViewById(R.id.profileButton)

        // Set click listener
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val surname = surnameEditText.text.toString().trim()
            val number = numberEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            // Simple validation
            if (name.isEmpty() || surname.isEmpty() || number.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Save user to database or shared preferences here

            Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()

            // Navigate to login screen
            findNavController().navigate(R.id.action_registerpageFragment_to_loginFragment)
        }
    }
}

