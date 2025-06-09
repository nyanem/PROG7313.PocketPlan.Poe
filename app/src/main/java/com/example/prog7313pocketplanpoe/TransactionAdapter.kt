package com.example.prog7313pocketplanpoe


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TransactionAdapter(
    private val items: List<com.example.prog7313pocketplanpoe.Transaction>
) : RecyclerView.Adapter<TransactionAdapter.VH>() {

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amount = itemView.findViewById<TextView>(R.id.textViewAmount)
        val category = itemView.findViewById<TextView>(R.id.textViewCategory)
        val date = itemView.findViewById<TextView>(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tranaction, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val t = items[position]
        holder.amount.text = "R%.2f".format(t.amount)
        holder.category.text = t.category
        holder.date.text = t.date
    }

    override fun getItemCount() = items.size
}

//class TransactionAdapter(private val transactions: List<Transaction>) :
//    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_tranaction, parent, false) // Use the correct layout name
//        return TransactionViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
//        holder.bind(transactions[position])
//    }
//
//    override fun getItemCount(): Int = transactions.size
//
//    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val amountText: TextView = itemView.findViewById(R.id.textViewAmount)
//        private val categoryText: TextView = itemView.findViewById(R.id.textViewCategory)
//        private val dateText: TextView = itemView.findViewById(R.id.textViewDate)
//
//        fun bind(transaction: Transaction) {
//            val formattedAmount = CurrencyManager.formatCurrency(itemView.context, transaction.amount)
//            amountText.text = formattedAmount
//            categoryText.text = transaction.category
//            dateText.text = transaction.date
//        }
//    }
//}
