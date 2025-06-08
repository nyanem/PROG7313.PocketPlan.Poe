
package com.example.prog7313pocketplanpoe

import android.content.Intent
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
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
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
                    .addOnSuccessListener {
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
// * Use the [SurveyFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class SurveyFragment : Fragment() {
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
//        return inflater.inflate(R.layout.fragment_survey, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SurveyFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SurveyFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}