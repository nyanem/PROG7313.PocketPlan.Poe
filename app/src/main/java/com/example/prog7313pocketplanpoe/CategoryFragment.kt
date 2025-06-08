package com.example.prog7313pocketplanpoe

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class CategoryFragment : Fragment() {
    class CategoryFragment : Fragment() {

        private lateinit var customCategoryEditText: EditText
        private lateinit var addCustomCategoryButton: Button
        private lateinit var selectButton: Button

        private lateinit var groceriesButton: Button
        private lateinit var rentButton: Button
        private lateinit var petrolButton: Button
        private lateinit var billsButton: Button
        private lateinit var homeButton: Button
        private lateinit var vacationButton: Button

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_category, container, false)

            // Bind Views
            groceriesButton = view.findViewById(R.id.Groceries)
            rentButton = view.findViewById(R.id.Rent)
            petrolButton = view.findViewById(R.id.Petrol)
            billsButton = view.findViewById(R.id.Bills)
            homeButton = view.findViewById(R.id.Home)
            vacationButton = view.findViewById(R.id.Vacation)

            customCategoryEditText = view.findViewById(R.id.editTextCustomCategory)
            addCustomCategoryButton = view.findViewById(R.id.btnAddCategory)
            selectButton = view.findViewById(R.id.selectCategoriesButton)

            // Add custom category logic
            addCustomCategoryButton.setOnClickListener {
                val customText = customCategoryEditText.text.toString().trim()
                if (customText.isNotEmpty()) {
                    // You can add this dynamically to a list or database
                    Toast.makeText(context, "Custom category added: $customText", Toast.LENGTH_SHORT).show()
                    customCategoryEditText.text.clear()
                }
            }

            // Example: handle select click
            selectButton.setOnClickListener {
                // Save selected categories
                Toast.makeText(context, "Categories selected!", Toast.LENGTH_SHORT).show()
                // Navigate to next screen or save to DB
            }

            return view
        }
    }

}