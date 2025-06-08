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
//        return inflater.inflate(R.layout.fragment_reports_page, container, false)
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