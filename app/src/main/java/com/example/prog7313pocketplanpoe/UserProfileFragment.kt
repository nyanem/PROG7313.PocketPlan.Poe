package com.example.prog7313pocketplanpoe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class UserProfileFragment : Fragment() {

    private val languageActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            requireActivity().recreate()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val helpBtn = view.findViewById<TextView>(R.id.menuHelp)
        val tipsBtn = view.findViewById<TextView>(R.id.menuTips)
        val languageBtn = view.findViewById<TextView>(R.id.menuLanguage)

        helpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_HelpFragment)
        }

        tipsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_TipsFragment)
        }

        languageBtn.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_languageFragment)
        }
    }
}