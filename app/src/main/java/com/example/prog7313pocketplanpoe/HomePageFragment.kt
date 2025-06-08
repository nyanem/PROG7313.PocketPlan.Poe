package com.example.prog7313pocketplanpoe



import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class HomePageFragment : Fragment() {

    private lateinit var container: LinearLayout
    private lateinit var budgetRemainingText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.homePageFragment)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        this.container = view.findViewById(R.id.categoryCardsContainer)
        budgetRemainingText = view.findViewById(R.id.budgetRemaining)

        val dbHelper = PocketPlanDBHelper(requireContext())
        val savedCategories = dbHelper.getAllCategories()
        val prefs = requireActivity().getSharedPreferences("UserData", android.content.Context.MODE_PRIVATE)
        val maxSavingGoal = dbHelper.getMaxSavingGoal()
        budgetRemainingText.text = "R${"%.2f".format(maxSavingGoal)}"

        for (category in savedCategories) {
            val goal = prefs.getString("${category}_goal", "0")?.toDoubleOrNull() ?: 0.0
            val spent = prefs.getString("${category}_spent", "0")?.toDoubleOrNull() ?: 0.0
            addCategoryCard(category, goal, spent)
        }

       // val addTransactionButton = view.findViewById<FloatingActionButton>(R.id.addTransaction)
        val filterButton = view.findViewById<Button>(R.id.filterButton)

//        addTransactionButton.setOnClickListener {
//            // Navigate to AddTransactionFragment if implemented
//           // Toast.makeText(requireContext(), "Transaction screen placeholder", Toast.LENGTH_SHORT).show()
//            findNavController().navigate(R.id.action_homePageFragment_to_AddTransactionFragment)
//        }

        filterButton.setOnClickListener { showDateRangePicker() }

        return view
    }

    private fun showDateRangePicker() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        DatePickerDialog(requireContext(), { _, startYear, startMonth, startDay ->
            val startDate = Calendar.getInstance()
            startDate.set(startYear, startMonth, startDay)

            DatePickerDialog(requireContext(), { _, endYear, endMonth, endDay ->
                val endDate = Calendar.getInstance()
                endDate.set(endYear, endMonth, endDay)

                val startStr = dateFormat.format(startDate.time)
                val endStr = dateFormat.format(endDate.time)

                filterCategoriesByDateRange(startStr, endStr)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun filterCategoriesByDateRange(startDate: String, endDate: String) {
        val dbHelper = PocketPlanDBHelper(requireContext())
        val filteredTotals = dbHelper.getCategoryTotalsBetweenDates(startDate, endDate)

        container.removeAllViews()

        for ((categoryName, totalAmount) in filteredTotals) {
            val cardView = LayoutInflater.from(requireContext()).inflate(R.layout.catergory_card, container, false)

            val categoryNameView = cardView.findViewById<TextView>(R.id.categoryName)
            val categorySpent = cardView.findViewById<TextView>(R.id.categorySpent)
            val categoryBalance = cardView.findViewById<TextView>(R.id.balance)
            val categoryProgress = cardView.findViewById<ProgressBar>(R.id.categoryProgress)
            val categoryGoalAmount = cardView.findViewById<TextView>(R.id.categoryGoalAmount)

            categoryNameView.text = categoryName
            categorySpent.text = "-R${"%.2f".format(totalAmount)}"
            val goal = 5000.0
            categoryGoalAmount.text = "Goal: R${"%.2f".format(goal)}"
            val balance = goal - totalAmount
            categoryBalance.text = "R${"%.2f".format(balance)}"
            categoryProgress.progress = if (goal > 0) ((totalAmount / goal) * 100).toInt().coerceAtMost(100) else 0

            container.addView(cardView)
        }
    }

    private fun addCategoryCard(categoryName: String, totalBudget: Double, amountSpent: Double) {
        val cardView = LayoutInflater.from(requireContext()).inflate(R.layout.catergory_card, container, false)

        val categoryNameView = cardView.findViewById<TextView>(R.id.categoryName)
        val categoryGoalAmount = cardView.findViewById<TextView>(R.id.categoryGoalAmount)
        val categorySpent = cardView.findViewById<TextView>(R.id.categorySpent)
        val categoryBalance = cardView.findViewById<TextView>(R.id.balance)
        val categoryProgress = cardView.findViewById<ProgressBar>(R.id.categoryProgress)

        val balance = totalBudget - amountSpent
        val progress = if (totalBudget > 0) ((amountSpent / totalBudget) * 100).toInt().coerceAtMost(100) else 0

        categoryNameView.text = categoryName
        categoryGoalAmount.text = "Goal: R${"%.2f".format(totalBudget)}"
        categorySpent.text = "-R${"%.2f".format(amountSpent)}"
        categoryBalance.text = "R${"%.2f".format(balance)}"
        categoryProgress.progress = progress

        container.addView(cardView)
    }
}

//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//
//class HomePageFragment : Fragment() {
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_home_page, container, false)
//    }
//}
