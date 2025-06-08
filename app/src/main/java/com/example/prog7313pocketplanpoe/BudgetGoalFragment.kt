
package com.example.prog7313pocketplanpoe

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.fragment.findNavController

class BudgetGoalFragment : Fragment() {

    private val inputMap = mutableMapOf<String, EditText>()
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget_goal, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val goalContainer = view.findViewById<LinearLayout>(R.id.goalContainer)
        val dbHelper = PocketPlanDBHelper(requireContext())
        val savedCategories = dbHelper.getAllCategories()
        val prefs = requireContext().getSharedPreferences("GoalPrefs", 0)

        db = FirebaseFirestore.getInstance()

        for (category in savedCategories) {
            val label = TextView(requireContext()).apply {
                text = category
                textSize = 17f
                setTextColor(Color.parseColor("#AAB6E0"))
                typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_semibold)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 20
                }
            }

            val input = EditText(requireContext()).apply {
                hint = "Enter amount"
                setTextColor(Color.BLACK)
                setHintTextColor(Color.parseColor("#6E79BA"))
                background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_edittext)
                setPadding(30, 30, 30, 30)
                layoutParams = LinearLayout.LayoutParams(
                    dpToPx(240),
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 10
                }
            }

            inputMap[category] = input
            goalContainer.addView(label)
            goalContainer.addView(input)
        }

        val saveGoalsButton = view.findViewById<Button>(R.id.saveGoalsButton)
        saveGoalsButton.setOnClickListener {
            val editor = prefs.edit()

            for ((category, input) in inputMap) {
                val amount = input.text.toString().trim()
                if (amount.isNotEmpty()) {
                    editor.putString(category, amount)

                    val goalData = hashMapOf(
                        "category" to category,
                        "goalAmount" to (amount.toDoubleOrNull() ?: 0.0),
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("categoryGoals")
                        .add(goalData)
                        .addOnSuccessListener {
                            // Optional success message
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Failed to save $category: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            editor.apply()

            Toast.makeText(requireContext(), "Goals saved successfully!", Toast.LENGTH_SHORT).show()

            // Navigate back to HomePageActivity if necessary
            findNavController().navigate(R.id.action_budgetGoalsFragment_to_HomePageFragment)
        }

        return view
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
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
// * Use the [BudgetGoalFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class BudgetGoalFragment : Fragment() {
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
//        return inflater.inflate(R.layout.fragment_budget_goal, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment BudgetGoalFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            BudgetGoalFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}