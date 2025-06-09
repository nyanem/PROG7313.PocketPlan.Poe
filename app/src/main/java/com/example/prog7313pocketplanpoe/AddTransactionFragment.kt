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
import com.google.firebase.firestore.FirebaseFirestore
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

        amountEditText = view.findViewById(R.id.amountEditText)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        dateEditText = view.findViewById(R.id.dateEditText)
        balanceEditText = view.findViewById(R.id.balanceEditText)
        uploadButton = view.findViewById(R.id.uploadButton)
        saveButton = view.findViewById(R.id.saveButton)

        setupCategorySpinner()
        setupDatePicker()

        saveButton.setOnClickListener {
            val amount = amountEditText.text.toString()
            val category = categorySpinner.selectedItem.toString()
            val date = dateEditText.text.toString()
            saveTransactions(amount, category, date)

            Toast.makeText(requireContext(), "Saved: $amount - $category on $date", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addTransactionFragment_to_homePageFragment)
        }

        uploadButton = view.findViewById(R.id.uploadButton)

        uploadButton.setOnClickListener {
            findNavController().navigate(R.id.action_AddtransactionFragment_to_addReceiptFragment)
        }



        balanceEditText.setText("R24000.00")

        return view
    }

    private fun setupCategorySpinner() {
        val dbHelper = PocketPlanDBHelper(requireContext())
        val savedCategories = dbHelper.getAllCategories()

        if (savedCategories.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No categories found. Please add categories first.", Toast.LENGTH_SHORT).show()
            return
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, savedCategories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

//    private fun setupCategorySpinner() {
//        val categories = listOf("Food", "Transport", "Entertainment", "Bills", "Other")
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        categorySpinner.adapter = adapter
//    }

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

    private fun saveTransactions(amount: String, category: String, date: String) {
        val dbHelper = TransactionDBHelper(requireContext())
        dbHelper.insertTransaction(category, amount.toDouble(), date)

        val db = FirebaseFirestore.getInstance()
        val transactionData = hashMapOf(
            "amount" to amount.toDouble(),
            "category" to category,
            "date" to date
        )

        db.collection("transactions")
            .add(transactionData)
            .addOnSuccessListener {
                view?.let {
                    Toast.makeText(it.context, "Transaction saved to Firestore!", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener {
                view?.let {
                    Toast.makeText(it.context, "Failed to save to Firestore", Toast.LENGTH_SHORT).show()
                }

            }
    }
}



//package com.example.prog7313pocketplanpoe
//
//import com.example.pocketplan.Category
//import com.example.pocketplan.Transaction
//import android.app.DatePickerDialog
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.fragment.app.Fragment
//import com.google.firebase.database.FirebaseDatabase
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.*
//import com.google.firebase.firestore.FirebaseFirestore
//
//class AddTransactionFragment : Fragment() {
//
//    private lateinit var dbHelper: PocketPlanDBHelper
//    private lateinit var balanceEditText: EditText
//    private lateinit var amountEditText: EditText
//    private lateinit var categorySpinner: Spinner
//    private lateinit var dateEditText: EditText
//    private lateinit var uploadReceiptButton: ImageButton
//    private lateinit var saveButton: Button
//
//
//    private var selectedCategory: Category? = null
//    private var currentPhotoPath: String? = null
//    private var receiptId: Int? = null
//
//    private val calendar = Calendar.getInstance()
//    private val firebaseDb = FirebaseDatabase.getInstance().reference
//    val db = FirebaseFirestore.getInstance()
//
//
////    private val takePictureLauncher = registerForActivityResult(
////        ActivityResultContracts.StartActivityForResult()
////    ) { result ->
////        if (result.resultCode == android.app.Activity.RESULT_OK) {
////            currentPhotoPath?.let {
////                receiptId = addReceipt(it)
////                Toast.makeText(requireContext(), "Receipt saved", Toast.LENGTH_SHORT).show()
////            }
////        }
////    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        dbHelper = PocketPlanDBHelper(requireContext())
//
//        balanceEditText = view.findViewById(R.id.balanceEditText)
//        amountEditText = view.findViewById(R.id.amountEditText)
//        categorySpinner = view.findViewById(R.id.categorySpinner)
//        dateEditText = view.findViewById(R.id.dateEditText)
//        uploadReceiptButton = view.findViewById(R.id.uploadButton)
//        saveButton = view.findViewById(R.id.saveButton)
//
//        balanceEditText.setText(getBalance())
//        balanceEditText.isEnabled = false
//
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        dateEditText.setText(dateFormat.format(Date()))
//
//        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
//            calendar.set(year, month, day)
//            updateDateInView()
//        }
//
//        dateEditText.setOnClickListener {
//            DatePickerDialog(
//                requireContext(),
//                dateSetListener,
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            ).show()
//        }
//
//        val categories = listOf("Food", "Transport", "Other")
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        categorySpinner.adapter = adapter
//
//        uploadReceiptButton.setOnClickListener {
//           // dispatchTakePictureIntent()
//        }
//
//        saveButton.setOnClickListener {
//            saveTransaction()
//        }
//    }
//
//    private fun updateDateInView() {
//        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        dateEditText.setText(dateFormat.format(calendar.time))
//    }
//
//    private fun createImageFile(): File {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
//            currentPhotoPath = absolutePath
//        }
//    }
//
////    private fun dispatchTakePictureIntent() {
////        val takePictureIntent = android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
////        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
////            val photoFile: File? = try {
////                createImageFile()
////            } catch (ex: Exception) {
////                null
////            }
////            photoFile?.also {
////                val photoURI = androidx.core.content.FileProvider.getUriForFile(
////                    requireContext(),
////                    "com.example.pocketplan.fileprovider",
////                    it
////                )
////                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
////                takePictureLauncher.launch(takePictureIntent)
////            }
////        }
////    }
//
//    private fun saveTransaction() {
//        val amountText = amountEditText.text.toString()
//        if (amountText.isEmpty()) {
//            Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val amount = amountText.toDoubleOrNull()
//        if (amount == null) {
//            Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val selectedCategory = categorySpinner.selectedItem?.toString() ?: "Other"
//        val date = dateEditText.text.toString()
//        val receiptUri = receiptId?.let { Uri.parse("content://com.example.pocketplan.fileprovider/$it") }
//
//        val transaction = Transaction(
//            id = 0,
//            amount = if (amount < 0) amount else -amount,
//            category = selectedCategory,
//            date = date,
//            receiptUri = receiptUri
//        )
//
//        // Save locally in SQLite
//        val transactionId: Long = dbHelper.addTransaction(transaction)
//        if (transactionId > 0L) {
//            // Prepare data for Firestore
//            val firestoreTransaction = hashMapOf(
//                "amount" to transaction.amount,
//                "category" to transaction.category,
//                "date" to transaction.date,
//                "receiptUri" to (transaction.receiptUri?.toString() ?: "")
//            )
//
//            // Save to Firestore
//            db.collection("transactions")
//                .add(firestoreTransaction)
//                .addOnSuccessListener {
//                    Toast.makeText(requireContext(), "Transaction saved (local & Firestore)", Toast.LENGTH_SHORT).show()
//                    requireActivity().supportFragmentManager.popBackStack()
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(requireContext(), "Saved locally but failed to sync to Firestore: ${e.message}", Toast.LENGTH_LONG).show()
//                }
//
//        } else {
//            Toast.makeText(requireContext(), "Error saving transaction", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    // ----- New methods you requested -----
//
//    private fun addTransaction(transaction: Transaction): Long {
//        return dbHelper.addTransaction(transaction)
//    }
////
////    private fun addReceipt(imagePath: String): Int {
////       // return dbHelper.addReceipt(imagePath).toInt()
////    }
//
//    private fun getBalance(): String {
//        val balance = dbHelper.getBalance()
//        // Use your CurrencyManager or fallback
//        return CurrencyManager.formatCurrency(requireContext(), balance)
//    }
//
//    // Optional: fallback currency formatting
//    private fun formatCurrency(amount: Double): String {
//        return String.format(Locale.getDefault(), "R%.2f", amount)
//    }
//}


