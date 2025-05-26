package com.example.testfin.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testfin.R
import com.example.testfin.adapters.CategoryAdapter
import com.example.testfin.data.CategoryItem
import com.example.testfin.data.Transaction
import com.example.testfin.helpers.SharedPrefHelper
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class CategoriesFragment : Fragment() {

    private lateinit var pieChartExpenses: PieChart
    private lateinit var pieChartIncome: PieChart
    private lateinit var rvIncome: RecyclerView
    private lateinit var rvExpenses: RecyclerView

    private lateinit var sharedPrefs: SharedPrefHelper
    private lateinit var transactions: List<Transaction>
    private lateinit var incomeCategoryList: List<CategoryItem>
    private lateinit var expenseCategoryList: List<CategoryItem>

    private val defaultIncomeCategories = listOf("Salary", "Bonus", "Interest", "Other")
    private val defaultExpenseCategories = listOf("Food", "Transport", "Bills", "Entertainment", "Shopping", "Health", "Savings", "Other")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        pieChartExpenses = view.findViewById(R.id.pieChartExpenses)
        pieChartIncome = view.findViewById(R.id.pieChartIncome)
        rvIncome = view.findViewById(R.id.rvIncome)
        rvExpenses = view.findViewById(R.id.rvExpenses)

        sharedPrefs = SharedPrefHelper(requireContext())

        ensureDefaultCategories()
        loadTransactions()
        generateCategorySummary()
        setupRecyclerViews()
        setupPieCharts()

        return view
    }

    private fun ensureDefaultCategories() {
        val prefs = requireContext().getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)
        if (!prefs.contains("categories_income")) {
            prefs.edit().putStringSet("categories_income", defaultIncomeCategories.toSet()).apply()
        }
        if (!prefs.contains("categories_expense")) {
            prefs.edit().putStringSet("categories_expense", defaultExpenseCategories.toSet()).apply()
        }
    }

    private fun loadTransactions() {
        transactions = sharedPrefs.getTransactions()
        android.util.Log.d("CategoriesFragment", "Loaded transactions: $transactions")
        Log.d("TransactionsDebug", "All transactions: $transactions")

    }

    private fun generateCategorySummary() {
        val incomeTransactions = transactions.filter { it.type == "Income" }
        val expenseTransactions = transactions.filter { it.type == "Expense" }

        val incomeTotals = incomeTransactions.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount.toDouble() } }

        val expenseTotals = expenseTransactions.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount.toDouble() } }

        val totalIncome = incomeTotals.values.sum()
        val totalExpense = expenseTotals.values.sum()

        incomeCategoryList = incomeTotals.map { (category, total) ->
            val percentage = if (totalIncome > 0.0) (total / totalIncome) * 100 else 0.0
            CategoryItem(category, total.toFloat(), percentage.toFloat())
        }.sortedByDescending { it.amount }

        expenseCategoryList = expenseTotals.map { (category, total) ->
            val percentage = if (totalExpense > 0.0) (total / totalExpense) * 100 else 0.0
            CategoryItem(category, total.toFloat(), percentage.toFloat())
        }.sortedByDescending { it.amount }

        Log.d("CategorySummary", "Expense category list: $expenseCategoryList")

    }

    private fun setupRecyclerViews() {
        rvExpenses.layoutManager = LinearLayoutManager(context)
        rvExpenses.adapter = CategoryAdapter(expenseCategoryList)

        rvIncome.layoutManager = LinearLayoutManager(context)
        rvIncome.adapter = CategoryAdapter(incomeCategoryList)


    }

    private fun setupPieCharts() {
        val incomeEntries = incomeCategoryList.map { PieEntry(it.amount, it.category) }
        val expenseEntries = expenseCategoryList.map { PieEntry(it.amount, it.category) }


        val incomeColors = listOf(
            android.graphics.Color.parseColor("#66BB6A"),
            android.graphics.Color.parseColor("#81C784"),
            android.graphics.Color.parseColor("#A5D6A7"),
            android.graphics.Color.parseColor("#C8E6C9")
        )

        val expenseColors = listOf(
            android.graphics.Color.parseColor("#E57373"),
            android.graphics.Color.parseColor("#EF5350"),
            android.graphics.Color.parseColor("#F44336"),
            android.graphics.Color.parseColor("#D32F2F")
        )

        val incomeDataSet = PieDataSet(incomeEntries, "Income").apply {
            colors = incomeColors
        }

        val expenseDataSet = PieDataSet(expenseEntries, "Expenses").apply {
            colors = expenseColors
        }

        pieChartIncome.data = PieData(incomeDataSet)
        pieChartIncome.description.isEnabled = false
        pieChartIncome.invalidate()

        pieChartExpenses.data = PieData(expenseDataSet)
        pieChartExpenses.description.isEnabled = false
        pieChartExpenses.invalidate()

        Log.d("PieChartDebug", "Expense entries: $expenseEntries")

    }
}
