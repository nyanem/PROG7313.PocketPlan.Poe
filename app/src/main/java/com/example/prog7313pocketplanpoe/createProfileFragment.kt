package com.example.prog7313pocketplanpoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class CreateProfileFragment : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var createProfileButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_profile, container, false)

        usernameEditText = view.findViewById(R.id.username_txtbox)
        passwordEditText = view.findViewById(R.id.password_txtbox)
        confirmPasswordEditText = view.findViewById(R.id.confirmPassword_txtbox)
        createProfileButton = view.findViewById(R.id.profileButton)

        createProfileButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Handle profile creation logic (e.g., save to DB)
                Toast.makeText(requireContext(), "Profile created successfully", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}