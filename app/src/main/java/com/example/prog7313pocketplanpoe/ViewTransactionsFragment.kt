package com.example.prog7313pocketplanpoe


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewTransactionsFragment : Fragment() {

    private lateinit var balanceTextView: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_transactions, container, false)

        balanceTextView = view.findViewById(R.id.balanceTextView)
        recyclerView = view.findViewById(R.id.transactionRecyclerView)

        // 1. Set a dummy balance
        balanceTextView.text = "R1,200.00"

        // 2. Dummy transaction data
        val dummyTransactions = listOf(
            Transaction("Groceries", "R250.00", "05/06/2025"),
            Transaction("Transport", "R75.00", "06/06/2025"),
            Transaction("Entertainment", "R120.00", "07/06/2025"),
            Transaction("Bills", "R300.00", "08/06/2025"),
            Transaction("Other", "R50.00", "09/06/2025")
        )

        // 3. Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = TransactionAdapter(dummyTransactions)

        return view
    }
}
