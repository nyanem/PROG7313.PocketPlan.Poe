
package com.example.prog7313pocketplanpoe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.prog7313pocketplanpoe.CategoryFragment
import com.example.prog7313pocketplanpoe.R
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.fragment.findNavController

class SurveyFragment : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_survey, container, false)

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.SurveyFragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val incomeBox = view.findViewById<EditText>(R.id.income_txtbox)
        val maxSavingsBox = view.findViewById<EditText>(R.id.maxsavings_txtbox)
        val minSavingsBox = view.findViewById<EditText>(R.id.minsavings_txtbox)
        val saveBtn = view.findViewById<Button>(R.id.saveSurveyButton)

        saveBtn.setOnClickListener {
            val income = incomeBox.text.toString().toDoubleOrNull()
            val max = maxSavingsBox.text.toString().trim().toDoubleOrNull()
            val min = minSavingsBox.text.toString().trim().toDoubleOrNull()

            if (income == null || max == null || min == null) {
                Toast.makeText(requireContext(), "Please input valid numbers", Toast.LENGTH_SHORT).show()
            } else {
                val surveyData = hashMapOf(
                    "income" to income,
                    "maxSavings" to max,
                    "minSavings" to min,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("surveys")
                    .add(surveyData)

                    //Preps for the reports page:Changed addOnSuccessListener to save locally.
                    .addOnSuccessListener {
                        val prefs = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
                        prefs.edit()
                            .putBoolean("hasCompletedSurvey", true)
                            .putFloat("minGoal", min.toFloat())
                            .putFloat("maxGoal", max.toFloat())
                            .apply()

                        Toast.makeText(requireContext(), "Survey saved online!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_surveyFragment_to_categoryFragment)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Failed to save survey: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }

        }

        return view
    }
}



