package com.example.prog7313pocketplanpoe


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketplan.Transaction // Ensure this is your correct model import

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tranaction, parent, false) // Use the correct layout name
        return TransactionViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amountText: TextView = itemView.findViewById(R.id.textViewAmount)
        private val categoryText: TextView = itemView.findViewById(R.id.textViewCategory)
        private val dateText: TextView = itemView.findViewById(R.id.textViewDate)

        fun bind(transaction: Transaction) {
            val formattedAmount = CurrencyManager.formatCurrency(itemView.context, transaction.amount)
            amountText.text = formattedAmount
            categoryText.text = transaction.category
            dateText.text = transaction.date
        }
    }
}
