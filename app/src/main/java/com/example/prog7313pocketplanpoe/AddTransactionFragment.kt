package com.example.prog7313pocketplanpoe

import com.example.pocketplan.Category
import com.example.pocketplan.Transaction
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore

class AddTransactionFragment : Fragment() {

    private lateinit var dbHelper: PocketPlanDBHelper
    private lateinit var balanceEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var dateEditText: EditText
    private lateinit var uploadReceiptButton: ImageButton
    private lateinit var saveButton: Button


    private var selectedCategory: Category? = null
    private var currentPhotoPath: String? = null
    private var receiptId: Int? = null

    private val calendar = Calendar.getInstance()
    private val firebaseDb = FirebaseDatabase.getInstance().reference
    val db = FirebaseFirestore.getInstance()


//    private val takePictureLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == android.app.Activity.RESULT_OK) {
//            currentPhotoPath?.let {
//                receiptId = addReceipt(it)
//                Toast.makeText(requireContext(), "Receipt saved", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = PocketPlanDBHelper(requireContext())

        balanceEditText = view.findViewById(R.id.balanceEditText)
        amountEditText = view.findViewById(R.id.amountEditText)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        dateEditText = view.findViewById(R.id.dateEditText)
        uploadReceiptButton = view.findViewById(R.id.uploadButton)
        saveButton = view.findViewById(R.id.saveButton)

        balanceEditText.setText(getBalance())
        balanceEditText.isEnabled = false

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateEditText.setText(dateFormat.format(Date()))

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(year, month, day)
            updateDateInView()
        }

        dateEditText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        val categories = listOf("Food", "Transport", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        uploadReceiptButton.setOnClickListener {
           // dispatchTakePictureIntent()
        }

        saveButton.setOnClickListener {
            saveTransaction()
        }
    }

    private fun updateDateInView() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateEditText.setText(dateFormat.format(calendar.time))
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

//    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
//            val photoFile: File? = try {
//                createImageFile()
//            } catch (ex: Exception) {
//                null
//            }
//            photoFile?.also {
//                val photoURI = androidx.core.content.FileProvider.getUriForFile(
//                    requireContext(),
//                    "com.example.pocketplan.fileprovider",
//                    it
//                )
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                takePictureLauncher.launch(takePictureIntent)
//            }
//        }
//    }

    private fun saveTransaction() {
        val amountText = amountEditText.text.toString()
        if (amountText.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null) {
            Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedCategory = categorySpinner.selectedItem?.toString() ?: "Other"
        val date = dateEditText.text.toString()
        val receiptUri = receiptId?.let { Uri.parse("content://com.example.pocketplan.fileprovider/$it") }

        val transaction = Transaction(
            id = 0,
            amount = if (amount < 0) amount else -amount,
            category = selectedCategory,
            date = date,
            receiptUri = receiptUri
        )

        // Save locally in SQLite
        val transactionId: Long = dbHelper.addTransaction(transaction)
        if (transactionId > 0L) {
            // Prepare data for Firestore
            val firestoreTransaction = hashMapOf(
                "amount" to transaction.amount,
                "category" to transaction.category,
                "date" to transaction.date,
                "receiptUri" to (transaction.receiptUri?.toString() ?: "")
            )

            // Save to Firestore
            db.collection("transactions")
                .add(firestoreTransaction)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Transaction saved (local & Firestore)", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Saved locally but failed to sync to Firestore: ${e.message}", Toast.LENGTH_LONG).show()
                }

        } else {
            Toast.makeText(requireContext(), "Error saving transaction", Toast.LENGTH_SHORT).show()
        }
    }


    // ----- New methods you requested -----

    private fun addTransaction(transaction: Transaction): Long {
        return dbHelper.addTransaction(transaction)
    }
//
//    private fun addReceipt(imagePath: String): Int {
//       // return dbHelper.addReceipt(imagePath).toInt()
//    }

    private fun getBalance(): String {
        val balance = dbHelper.getBalance()
        // Use your CurrencyManager or fallback
        return CurrencyManager.formatCurrency(requireContext(), balance)
    }

    // Optional: fallback currency formatting
    private fun formatCurrency(amount: Double): String {
        return String.format(Locale.getDefault(), "R%.2f", amount)
    }
}


//Default code - do not use
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [AddTransactionFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class AddTransactionFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment AddTransactionFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AddTransactionFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}