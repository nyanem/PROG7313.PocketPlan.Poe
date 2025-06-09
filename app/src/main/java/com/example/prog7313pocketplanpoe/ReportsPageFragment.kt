package com.example.prog7313pocketplanpoe

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.util.TableInfo.Column
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class ReportsPageFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_reports_page, container, false)

        val rewardsButton = view.findViewById<Button>(R.id.rewardsButton)
        rewardsButton.setOnClickListener {
            findNavController().navigate(R.id.action_reportspageFragment_to_rewardsFragment)
        }

        return view
    }

    private fun setupChart() {
        val chart = view?.findViewById<BarChart>(R.id.spendingChart)

        val categorySpending = mapOf(
            "Food" to 300f,
            "Transport" to 180f,
            "Entertainment" to 220f
        )

        val barEntries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        var index = 0f
        for ((category, amount) in categorySpending) {
            barEntries.add(BarEntry(index, amount))
            labels.add(category)
            index++
        }

        val dataSet = BarDataSet(barEntries, "Spending")
        dataSet.color = ContextCompat.getColor(requireContext(), R.color.teal_700)

        // Add goal lines
        val maxGoal = 500f
        val minGoal = 100f

        val maxGoalLine = LimitLine(maxGoal, "Max Goal")
        maxGoalLine.lineColor = Color.RED
        maxGoalLine.lineWidth = 2f

        val minGoalLine = LimitLine(minGoal, "Min Goal")
        minGoalLine.lineColor = Color.GREEN
        minGoalLine.lineWidth = 2f

        val yAxis = chart?.axisLeft
        yAxis?.removeAllLimitLines()
        yAxis?.addLimitLine(maxGoalLine)
        yAxis?.addLimitLine(minGoalLine)

        chart?.axisRight?.isEnabled = false

        val data = BarData(dataSet)
        chart?.data = data

        val xAxis = chart?.xAxis
        xAxis?.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.granularity = 1f
        xAxis?.setDrawGridLines(false)

        chart?.description?.isEnabled = false
        chart?.invalidate()
    }
}