package com.example.prog7313pocketplanpoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class SurveyFragment : Fragment() {

    private lateinit var incomeEditText: EditText
    private lateinit var maxSavingsEditText: EditText
    private lateinit var minSavingsEditText: EditText
    private lateinit var saveSurveyButton: Button
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_survey, container, false)

        incomeEditText = view.findViewById(R.id.income_txtbox)
        maxSavingsEditText = view.findViewById(R.id.maxsavings_txtbox)
        minSavingsEditText = view.findViewById(R.id.minsavings_txtbox)
        saveSurveyButton = view.findViewById(R.id.saveSurveyButton)
        bottomNav = view.findViewById(R.id.bottomNav)

        saveSurveyButton.setOnClickListener {
            val income = incomeEditText.text.toString()
            val maxSavings = maxSavingsEditText.text.toString()
            val minSavings = minSavingsEditText.text.toString()

            // TODO: Save these values locally or send to ViewModel / Database
        }

        return view
    }
}
