package com.example.prog7313pocketplanpoe

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prog7313pocketplanpoe.CurrencyManager.formatCurrency
import java.text.SimpleDateFormat
import java.util.*

class ViewTransactionsFragment : Fragment() {

    private lateinit var transactionList: RecyclerView
    private lateinit var balanceText: TextView
    private lateinit var rewardsButton: Button
    private lateinit var dbHelper: TransactionDBHelper
    private lateinit var adapter: TransactionAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_transactions, container, false)

        // Initialize DatabaseHelper
        dbHelper = TransactionDBHelper(requireContext())

        // Initialize views
        balanceText = view.findViewById(R.id.balanceTextView)
        transactionList = view.findViewById(R.id.transactionRecyclerView)
        rewardsButton = view.findViewById(R.id.btn_rewards)
        transactionList = view.findViewById(R.id.transactionRecyclerView)
        transactionList.layoutManager = LinearLayoutManager(requireContext())


        // Set up RecyclerView
        transactionList.layoutManager = LinearLayoutManager(requireContext())

        val filterButton: Button = view.findViewById(R.id.filterTransactionsButton)
        filterButton.setOnClickListener {
            showDateRangeTransactionFilter()
        }


        // Load and display transactions
        loadTransactions()

        // Rewards button navigation
        rewardsButton.setOnClickListener {
            findNavController().navigate(R.id.action_viewTransactionsFragment_to_RewardsFragment)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
    }

    private fun loadTransactions() {
        val transactions = dbHelper.getAllTransactions()
        val balance = transactions.sumOf { it.amount }
        balanceText.text = formatCurrency(requireContext(), balance)
        adapter = TransactionAdapter(transactions)
        transactionList.adapter = adapter
    }




    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun showDateRangeTransactionFilter() {
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

                // Get filtered transactions from database
                val transactions = dbHelper.getTransactionsByDateRange(startStr, endStr)

                // Update balance text if you show it
                val balance = transactions.sumOf { it.amount }
                balanceText.text = "R%.2f".format(balance)

                // Update RecyclerView
                adapter = TransactionAdapter(transactions)
                transactionList.adapter = adapter

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }



}



//package com.example.prog7313pocketplanpoe
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.prog7313pocketplanpoe.CurrencyManager.formatCurrency
//
//class ViewTransactionsFragment : Fragment() {
//
//    private lateinit var transactionList: RecyclerView
//    private lateinit var balanceText: TextView
//    private lateinit var rewardsButton: Button
//    private lateinit var dbHelper: PocketPlanDBHelper
//    private lateinit var adapter: TransactionAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_view_transactions, container, false)
//
//        // Initialize DatabaseHelper
//        dbHelper = PocketPlanDBHelper(requireContext())
//
//
//        // Initialize views
//        balanceText = view.findViewById(R.id.balanceTextView)
//        transactionList = view.findViewById(R.id.transactionRecyclerView)
//        rewardsButton = view.findViewById(R.id.btn_rewards)
//
//        // Set up RecyclerView
//        transactionList.layoutManager = LinearLayoutManager(requireContext())
//
//        // Load and display transactions
//        loadTransactions()
//
//        // Set up click listener for rewards button
//        rewardsButton.setOnClickListener {
//            findNavController().navigate(R.id.action_viewTransctionsFragment_to_RewardsFragment)
//        }
//
//        return view
//    }
//
//    override fun onResume() {
//        super.onResume()
//        loadTransactions()
//    }
//
//    private fun loadTransactions() {
//        val transactions = dbHelper.getAllTransactions()
//        val balance = transactions.sumOf { it.amount }
//        balanceText.text = formatCurrency(requireContext(), balance)
//        adapter = TransactionAdapter(transactions)
//        transactionList.adapter = adapter
//    }
//}

