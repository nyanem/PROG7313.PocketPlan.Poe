package com.example.prog7313pocketplanpoe

import android.R.attr.textColor
import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ReportsPageFragment : Fragment() {

    private lateinit var dbHelper: BudgetGoalDBHelper
    private lateinit var rewardsButton: Button
    private lateinit var reportFilterButton: Button



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reports_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = BudgetGoalDBHelper(requireContext())
        rewardsButton = view.findViewById(R.id.btn_rewards)
        reportFilterButton = view.findViewById(R.id.reportFilterButton)

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.graphContainer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        setupBarChart(view)

        rewardsButton.setOnClickListener {
            findNavController().navigate(R.id.action_viewReportsPageFragment_to_RewardsFragment)
        }

        val filterButton = view.findViewById<Button>(R.id.reportFilterButton)

        filterButton.setOnClickListener {
            showStartDatePicker()

        }

    }
    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(year, month, dayOfMonth)
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                onDateSelected(formatter.format(selectedCalendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }


    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val startDate = Calendar.getInstance()
                startDate.set(year, month, day)
                showEndDatePicker(startDate.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showEndDatePicker(startDate: Date) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val endDate = Calendar.getInstance()
                endDate.set(year, month, day)

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val startDateStr = sdf.format(startDate)
                val endDateStr = sdf.format(endDate.time)

                Toast.makeText(requireContext(), "Start: $startDateStr\nEnd: $endDateStr", Toast.LENGTH_LONG).show()

               setupBarChart(requireView())

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
//    private fun setupBarChart(view: View) {
//        val barChart = view.findViewById<BarChart>(R.id.barChart)
//
//        val categories = listOf("Food", "Transport", "Entertainment", "Bills")
//        val spentAmounts = listOf(500f, 200f, 150f, 300f)
//        val minGoals = listOf(100f, 50f, 50f, 100f)
//        val maxGoals = listOf(600f, 300f, 250f, 400f)
//
//        val spentEntries = ArrayList<BarEntry>()
//        val minGoalEntries = ArrayList<BarEntry>()
//        val maxGoalEntries = ArrayList<BarEntry>()
//
//        for (i in categories.indices) {
//            spentEntries.add(BarEntry(i.toFloat(), spentAmounts[i]))
//            minGoalEntries.add(BarEntry(i.toFloat(), minGoals[i]))
//            maxGoalEntries.add(BarEntry(i.toFloat(), maxGoals[i]))
//        }
//
//        val spentSet = BarDataSet(spentEntries, "Spent").apply { color = Color.BLUE }
//        val minSet = BarDataSet(minGoalEntries, "Min Goal").apply { color = Color.GREEN }
//        val maxSet = BarDataSet(maxGoalEntries, "Max Goal").apply { color = Color.RED }
//
//        val data = BarData(spentSet, minSet, maxSet).apply { barWidth = 0.2f }
//        data.groupBars(0f, 0.2f, 0.05f)
//
//        barChart.data = data
//
//        val xAxis = barChart.xAxis
//        xAxis.valueFormatter = IndexAxisValueFormatter(categories)
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.granularity = 1f
//        xAxis.setCenterAxisLabels(true)
//        xAxis.axisMinimum = 0f
//        xAxis.axisMaximum = categories.size.toFloat()
//        xAxis.labelRotationAngle = -30f
//
//        barChart.axisRight.isEnabled = false
//        barChart.description.isEnabled = false
//        barChart.setVisibleXRangeMaximum(categories.size.toFloat())
//        barChart.invalidate()
//    }
//    private fun setupBarChart(view: View) {
//        val barChart = view.findViewById<BarChart>(R.id.barChart)
//
//        val dbHelper = BudgetGoalDBHelper(requireContext())
//        val pocketPlanDbHelper = PocketPlanDBHelper(requireContext())
//        val goals = dbHelper.getAllGoals()
//
//        val (monthlyMinGoal, monthlyMaxGoal) = pocketPlanDbHelper.getMonthlyGoalRange()
//
//        val categories = goals.map { it.category }
//        val entries = goals.mapIndexed { index, goal ->
//            BarEntry(index.toFloat(), goal.goalAmount.toFloat())
//        }
//
//        val dataSet = BarDataSet(entries, "Spent").apply {
//            color = Color.parseColor("#928EC5")
//            valueTextSize = 14f
//        }
//
//        val data = BarData(dataSet)
//        data.barWidth = 0.4f
//
//        barChart.data = data
//
//        // X-Axis setup
//        val xAxis = barChart.xAxis
//        xAxis.valueFormatter = IndexAxisValueFormatter(categories)
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.granularity = 1f
//        xAxis.setDrawGridLines(false)
//        xAxis.textSize = 12f
//        xAxis.labelRotationAngle = -30f
//
//        // Y-Axis setup
//        val yAxisLeft = barChart.axisLeft
//        yAxisLeft.textSize = 12f
//        yAxisLeft.removeAllLimitLines()
//
//        // Add dashed limit lines
//        // Add dashed limit lines
//        val minGoalLine = LimitLine(monthlyMinGoal, "Min Goal").apply {
//            lineColor = Color.parseColor("#4CAF50") // green
//            lineWidth = 2f
//            enableDashedLine(10f, 10f, 0f)
//            textSize = 15f
//            textColor = Color.parseColor("#4CAF50")
//            labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
//        }
//
//        val maxGoalLine = LimitLine(monthlyMaxGoal, "Max Goal").apply {
//            lineColor = Color.parseColor("#F44336") // red
//            lineWidth = 2f
//            enableDashedLine(10f, 10f, 0f)
//            textSize = 15f
//            textColor = Color.parseColor("#F44336")
//            labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
//        }
//
//        yAxisLeft.addLimitLine(minGoalLine)
//        yAxisLeft.addLimitLine(maxGoalLine)
//
//        barChart.axisRight.isEnabled = false
//        barChart.description.isEnabled = false
//        barChart.setVisibleXRangeMaximum(categories.size.toFloat())
//        barChart.invalidate()
//    }

    private fun setupBarChart(view: View) {
        val barChart = view.findViewById<BarChart>(R.id.barChart)

        val dbHelper = BudgetGoalDBHelper(requireContext())
        val pocketPlanDbHelper = PocketPlanDBHelper(requireContext())
        val goals = dbHelper.getAllGoals()

        val (monthlyMinGoal, monthlyMaxGoal) = pocketPlanDbHelper.getMonthlyGoalRange()

        val categories = goals.map { it.category }
        val entries = goals.mapIndexed { index, goal ->
            BarEntry(index.toFloat(), goal.goalAmount.toFloat())
        }



        val dataSet = BarDataSet(entries, "Spent").apply {
            color = Color.parseColor("#7886C7")
            valueTextSize = 20f
        }

        val data = BarData(dataSet)
        data.barWidth = 0.4f

        barChart.data = data

        // X-Axis setup
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(categories)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 15f
        xAxis.textColor = Color.parseColor("#FFF3F3")
        xAxis.labelRotationAngle = 0f

        // Y-Axis setup
        val yAxisLeft = barChart.axisLeft
        yAxisLeft.textSize = 18f
        yAxisLeft.textColor = Color.parseColor("#FFF3F3")
        yAxisLeft.removeAllLimitLines()


        // Add dashed limit lines
        val minGoalLine = LimitLine(monthlyMinGoal, "Min Goal").apply {
            lineColor = Color.parseColor("#4CAF50") // green
            lineWidth = 2f
            enableDashedLine(10f, 10f, 0f)
            textSize = 15f
            textColor = Color.parseColor("#4CAF50")
            labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
        }

        val maxGoalLine = LimitLine(monthlyMaxGoal, "Max Goal").apply {
            lineColor = Color.parseColor("#F44336") // red
            lineWidth = 2f
            enableDashedLine(10f, 10f, 0f)
            textSize = 15f
            textColor = Color.parseColor("#F44336")
            labelPosition = LimitLine.LimitLabelPosition.LEFT_TOP
        }

        yAxisLeft.removeAllLimitLines()
        yAxisLeft.addLimitLine(minGoalLine)
        yAxisLeft.addLimitLine(maxGoalLine)


        val maxYValue = (entries.maxOfOrNull { it.y } ?: 0f)
        val calculatedMax = maxOf(maxYValue, monthlyMaxGoal) + 10f
        yAxisLeft.axisMinimum = 0f
        yAxisLeft.axisMaximum = calculatedMax

        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setVisibleXRangeMaximum(categories.size.toFloat())
        barChart.invalidate()


    }


}


//package com.example.prog7313pocketplanpoe
//
//import android.graphics.Color
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.fragment.app.Fragment
//import com.example.prog7313pocketplanpoe.R
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
//
//class ReportsPageFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_reports_page, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Set padding for system bars
//        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.graphContainer)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        setupBarChart(view)
//    }
//
//    private fun setupBarChart(view: View) {
//        val barChart = view.findViewById<BarChart>(R.id.barChart)
//
//        val categories = listOf("Food", "Transport", "Entertainment", "Bills")
//        val spentAmounts = listOf(500f, 200f, 150f, 300f)
//        val minGoals = listOf(100f, 50f, 50f, 100f)
//        val maxGoals = listOf(600f, 300f, 250f, 400f)
//
//        val spentEntries = ArrayList<BarEntry>()
//        val minGoalEntries = ArrayList<BarEntry>()
//        val maxGoalEntries = ArrayList<BarEntry>()
//
//        for (i in categories.indices) {
//            spentEntries.add(BarEntry(i.toFloat(), spentAmounts[i]))
//            minGoalEntries.add(BarEntry(i.toFloat(), minGoals[i]))
//            maxGoalEntries.add(BarEntry(i.toFloat(), maxGoals[i]))
//        }
//
//        val spentSet = BarDataSet(spentEntries, "Spent").apply { color = Color.BLUE }
//        val minSet = BarDataSet(minGoalEntries, "Min Goal").apply { color = Color.GREEN }
//        val maxSet = BarDataSet(maxGoalEntries, "Max Goal").apply { color = Color.RED }
//
//        val data = BarData(spentSet, minSet, maxSet).apply { barWidth = 0.2f }
//        data.groupBars(0f, 0.2f, 0.05f)
//
//        barChart.data = data
//
//        val xAxis = barChart.xAxis
//        xAxis.valueFormatter = IndexAxisValueFormatter(categories)
//        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.granularity = 1f
//        xAxis.setCenterAxisLabels(true)
//        xAxis.axisMinimum = 0f
//        xAxis.axisMaximum = categories.size.toFloat()
//        xAxis.labelRotationAngle = -30f
//
//        barChart.axisRight.isEnabled = false
//        barChart.description.isEnabled = false
//        barChart.setVisibleXRangeMaximum(categories.size.toFloat())
//        barChart.invalidate()
//    }


//package com.example.prog7313pocketplanpoe
//
//import android.graphics.Color
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.room.util.TableInfo.Column
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.components.LimitLine
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
//
//
//class ReportsPageFragment : Fragment() {
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//    }
//
//
//    private fun setupChart() {
//        val chart = view?.findViewById<BarChart>(R.id.spendingChart)
//
//        val categorySpending = mapOf(
//            "Food" to 300f,
//            "Transport" to 180f,
//            "Entertainment" to 220f
//        )
//
//        val barEntries = mutableListOf<BarEntry>()
//        val labels = mutableListOf<String>()
//
//        var index = 0f
//        for ((category, amount) in categorySpending) {
//            barEntries.add(BarEntry(index, amount))
//            labels.add(category)
//            index++
//        }
//
//        val dataSet = BarDataSet(barEntries, "Spending")
//        dataSet.color = ContextCompat.getColor(requireContext(), R.color.teal_700)
//
//        // Add goal lines
//        val maxGoal = 500f
//        val minGoal = 100f
//
//        val maxGoalLine = LimitLine(maxGoal, "Max Goal")
//        maxGoalLine.lineColor = Color.RED
//        maxGoalLine.lineWidth = 2f
//
//        val minGoalLine = LimitLine(minGoal, "Min Goal")
//        minGoalLine.lineColor = Color.GREEN
//        minGoalLine.lineWidth = 2f
//
//        val yAxis = chart?.axisLeft
//        yAxis?.removeAllLimitLines()
//        yAxis?.addLimitLine(maxGoalLine)
//        yAxis?.addLimitLine(minGoalLine)
//
//        chart?.axisRight?.isEnabled = false
//
//        val data = BarData(dataSet)
//        chart?.data = data
//
//        val xAxis = chart?.xAxis
//        xAxis?.valueFormatter = IndexAxisValueFormatter(labels)
//        xAxis?.position = XAxis.XAxisPosition.BOTTOM
//        xAxis?.granularity = 1f
//        xAxis?.setDrawGridLines(false)
//
//        chart?.description?.isEnabled = false
//        chart?.invalidate()
//    }
//}
//
//
////package com.example.prog7313pocketplanpoe
////
////import android.os.Bundle
////import androidx.fragment.app.Fragment
////import android.view.LayoutInflater
////import android.view.View
////import android.view.ViewGroup
////
////// TODO: Rename parameter arguments, choose names that match
////// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
////private const val ARG_PARAM1 = "param1"
////private const val ARG_PARAM2 = "param2"
////
/////**
//// * A simple [Fragment] subclass.
//// * Use the [ReportsPageFragment.newInstance] factory method to
//// * create an instance of this fragment.
//// */
////class ReportsPageFragment : Fragment() {
////    // TODO: Rename and change types of parameters
////    private var param1: String? = null
////    private var param2: String? = null
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        arguments?.let {
////            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
////        }
////    }
////
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_reports_page, container, false)
////    }
////
////    companion object {
////        /**
////         * Use this factory method to create a new instance of
////         * this fragment using the provided parameters.
////         *
////         * @param param1 Parameter 1.
////         * @param param2 Parameter 2.
////         * @return A new instance of fragment ReportsPageFragment.
////         */
////        // TODO: Rename and change types and number of parameters
////        @JvmStatic
////        fun newInstance(param1: String, param2: String) =
////            ReportsPageFragment().apply {
////                arguments = Bundle().apply {
////                    putString(ARG_PARAM1, param1)
////                    putString(ARG_PARAM2, param2)
////                }
////            }
////    }
////}