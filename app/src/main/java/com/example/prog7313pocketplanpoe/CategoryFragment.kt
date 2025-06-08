package com.example.prog7313pocketplanpoe

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.prog7313pocketplanpoe.CategoryDBHelper
import com.example.prog7313pocketplanpoe.R
import com.google.firebase.firestore.FirebaseFirestore

class CategoryFragment : Fragment() {

    private lateinit var dbHelper: CategoryDBHelper
    private val selectedCategories = mutableListOf<String>()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        dbHelper = CategoryDBHelper(requireContext())

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttons = listOf(
            Pair(R.id.Groceries, "Groceries"),
            Pair(R.id.Rent, "Rent"),
            Pair(R.id.Petrol, "Petrol"),
            Pair(R.id.Bills, "Bills"),
            Pair(R.id.Home, "Home"),
            Pair(R.id.Vacation, "Vacation")
        )

        buttons.forEach { (id, categoryName) ->
            view.findViewById<Button>(id).setOnClickListener {
                if (!selectedCategories.contains(categoryName)) {
                    selectedCategories.add(categoryName)
                    it.setBackgroundColor(Color.parseColor("#5C6BC0"))
                    Toast.makeText(requireContext(), "$categoryName selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val customBtn = view.findViewById<Button>(R.id.btnAddCategory)
        val customInput = view.findViewById<EditText>(R.id.editTextCustomCategory)

        customBtn.setOnClickListener {
            val customName = customInput.text.toString().trim()
            if (customName.isNotEmpty() && !selectedCategories.contains(customName)) {
                selectedCategories.add(customName)
                Toast.makeText(requireContext(), "$customName added", Toast.LENGTH_SHORT).show()
                customInput.setText("")
            }
        }

        val selectBtn = view.findViewById<Button>(R.id.selectCategoriesButton)
        selectBtn.setOnClickListener {
            if (selectedCategories.isEmpty()) {
                Toast.makeText(requireContext(), "Please select or add categories", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save locally
            selectedCategories.forEach { dbHelper.insertCategory(it) }

            // Save online
            val categoriesMap = hashMapOf(
                "categories" to selectedCategories,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("selectedCategories")
                .add(categoriesMap)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Categories saved online!", Toast.LENGTH_SHORT).show()


                    findNavController().navigate(R.id.action_categoryFragment_to_budgetGoalsFragment)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to save categories: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        return view
    }
}




//Default code - do not use
//package com.example.prog7313pocketplanpoe
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [CategoryFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class CategoryFragment : Fragment() {
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
//        return inflater.inflate(R.layout.fragment_category, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment CategoryFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            CategoryFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}