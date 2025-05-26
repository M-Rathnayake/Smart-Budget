package com.example.testfin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testfin.R
import com.example.testfin.adapters.TransactionAdapter
import com.example.testfin.data.Transaction
import com.example.testfin.helpers.SharedPrefHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class TransactionsFragment : Fragment() {

    private lateinit var rvTransactions: RecyclerView
    private lateinit var tvEmptyMessage: TextView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var sharedPrefs: SharedPrefHelper
    private var transactions = mutableListOf<Transaction>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transactions, container, false)

        rvTransactions = view.findViewById(R.id.rvTransactions)
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage)
        fabAdd = view.findViewById(R.id.fabAddTransaction)
        sharedPrefs = SharedPrefHelper(requireContext())

        transactions = sharedPrefs.getTransactions().toMutableList()

        setupRecyclerView()
        showEmptyStateIfNeeded()

        fabAdd.setOnClickListener {
            showTransactionDialog(null)
        }

        return view
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(transactions,
            onEdit = { showTransactionDialog(it) },
            onDelete = { deleteTransaction(it) }
        )
        rvTransactions.layoutManager = LinearLayoutManager(context)
        rvTransactions.adapter = transactionAdapter
    }

    private fun showEmptyStateIfNeeded() {
        tvEmptyMessage.visibility = if (transactions.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun deleteTransaction(transaction: Transaction) {
        transactions.remove(transaction)
        sharedPrefs.saveTransactions(transactions)
        transactionAdapter.notifyDataSetChanged()
        showEmptyStateIfNeeded()
    }

    private fun showTransactionDialog(existing: Transaction?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_transaction, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etAmount = dialogView.findViewById<EditText>(R.id.etAmount)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinnerCategory)
        val radioGroupType = dialogView.findViewById<RadioGroup>(R.id.rgTransactionType)

        // Initialize default selection
        var currentType = if (existing?.type == "Expense") "Expense" else "Income"
        val categories = if (currentType == "Expense") sharedPrefs.getExpenseCategories() else sharedPrefs.getIncomeCategories()
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter

        // Update spinner when radio button is changed
        radioGroupType.setOnCheckedChangeListener { _, checkedId ->
            currentType = if (checkedId == R.id.rbExpense) "Expense" else "Income"
            val updatedCategories = if (currentType == "Expense") sharedPrefs.getExpenseCategories() else sharedPrefs.getIncomeCategories()
            spinnerAdapter.clear()
            spinnerAdapter.addAll(updatedCategories)
            spinnerAdapter.notifyDataSetChanged()
        }

        if (existing != null) {
            etTitle.setText(existing.title)
            etAmount.setText(existing.amount.toString())
            val index = categories.indexOf(existing.category)
            if (index >= 0) spinnerCategory.setSelection(index)

            if (existing.type == "Income") {
                radioGroupType.check(R.id.rbIncome)
            } else {
                radioGroupType.check(R.id.rbExpense)
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (existing == null) "Add Transaction" else "Edit Transaction")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val title = etTitle.text.toString()
                val amount = etAmount.text.toString().toFloatOrNull() ?: 0f
                val category = spinnerCategory.selectedItem.toString()
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val type = if (radioGroupType.checkedRadioButtonId == R.id.rbExpense) "Expense" else "Income"

                if (existing != null) transactions.remove(existing)

                val transaction = Transaction(title, amount, category, date, type)
                transactions.add(transaction)
                sharedPrefs.saveTransactions(transactions)
                transactionAdapter.notifyDataSetChanged()
                showEmptyStateIfNeeded()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
