package com.example.testfin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testfin.R
import com.example.testfin.data.Transaction

class TransactionAdapter(
    private val transactions: MutableList<Transaction>,
    private val onEdit: (Transaction) -> Unit,
    private val onDelete: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTransactionTitle)
        val tvAmount: TextView = view.findViewById(R.id.tvTransactionAmount)
        val tvCategory: TextView = view.findViewById(R.id.tvTransactionCategory)
        val btnEdit: Button = view.findViewById(R.id.btnEditTransaction)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteTransaction)
        val layoutActions: LinearLayout = view.findViewById(R.id.layoutTransactionActions)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

//    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
//        val transaction = transactions[position]
//        holder.tvTitle.text = transaction.title
//        holder.tvAmount.text = "Rs. %.2f".format(transaction.amount)
//        holder.tvCategory.text = transaction.category
//
//        holder.btnEdit.setOnClickListener { onEdit(transaction) }
//        holder.btnDelete.setOnClickListener { onDelete(transaction) }
//    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.tvTitle.text = transaction.title
        holder.tvAmount.text = "Rs. %.2f".format(transaction.amount)
        holder.tvCategory.text = transaction.category
        //holder.tvDate.text = transaction.date

        // Set the initial visibility of the action buttons to GONE
        holder.layoutActions.visibility = View.GONE

        // Set a long click listener to show the action buttons
        holder.itemView.setOnLongClickListener {
            // Toggle visibility when long pressed
            holder.layoutActions.visibility = if (holder.layoutActions.visibility == View.GONE) View.VISIBLE else View.GONE
            true // Return true to indicate the event was handled
        }

        // Set listeners for the Edit and Delete buttons
        holder.btnEdit.setOnClickListener { onEdit(transaction) }
        holder.btnDelete.setOnClickListener { onDelete(transaction) }
    }


    override fun getItemCount(): Int = transactions.size
}
