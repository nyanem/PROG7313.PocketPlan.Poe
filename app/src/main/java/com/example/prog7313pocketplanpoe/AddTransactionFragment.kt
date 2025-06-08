package com.example.prog7313pocketplanpoe

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.util.Calendar

class AddTransactionFragment : Fragment() {

    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var dateEditText: EditText
    private lateinit var balanceEditText: EditText
    private lateinit var uploadButton: ImageButton
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        amountEditText = view.findViewById(R.id.amountedittext)
        categorySpinner = view.findViewById(R.id.categoryspinner)
        dateEditText = view.findViewById(R.id.dateedittext)
        balanceEditText = view.findViewById(R.id.balanceedittext)
        uploadButton = view.findViewById(R.id.uploadButton)
        saveButton = view.findViewById(R.id.saveutton)

        setupCategorySpinner()
        setupDatePicker()

        saveButton.setOnClickListener {
            val amount = amountEditText.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val date = dateEditText.text.toString()
            saveTransactionToFile(amount, category, date)

            Toast.makeText(requireContext(), "Saved: $amount - $category on $date", Toast.LENGTH_SHORT).show()
        }

        uploadButton = view.findViewById(R.id.uploadButton)
        uploadButton.setOnClickListener {
            findNavController().navigate(R.id.action_AddtransactionFragment_to_addReceiptFragment)
        }


        // Example balance load
        balanceEditText.setText("R1200.00") // You can set this dynamically later

        return view
    }

    private fun setupCategorySpinner() {
        val categories = listOf("Food", "Transport", "Entertainment", "Bills", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun setupDatePicker() {
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    dateEditText.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }

    private fun saveTransactionToFile(amount: String, category: String, date: String) {
        val transaction = "$amount - $category on $date\n"
        requireContext().openFileOutput("transactions.txt", android.content.Context.MODE_APPEND).use {
            it.write(transaction.toByteArray())
        }
    }
}

