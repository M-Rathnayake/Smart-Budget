package com.example.testfin.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.testfin.ProfileActivity
import com.example.testfin.R
import com.example.testfin.helpers.SharedPrefAuthHelper
import com.example.testfin.helpers.SharedPrefHelper
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class DashboardFragment : Fragment() {

    private var tvIncome: TextView? = null
    private var tvExpense: TextView? = null
    private var tvBalance: TextView? = null
    private var tvUsername: TextView? = null
    private lateinit var barChart: BarChart  // Changed to BarChart

    private lateinit var sharedPrefs: SharedPrefHelper
    private lateinit var authPrefs: SharedPrefAuthHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        tvIncome = view.findViewById(R.id.tvIncome)
        tvExpense = view.findViewById(R.id.tvExpense)
        tvBalance = view.findViewById(R.id.tvBalance)
        tvUsername = view.findViewById(R.id.textView2)
        barChart = view.findViewById(R.id.barChart)  // Initialize BarChart

        sharedPrefs = SharedPrefHelper(requireContext())
        authPrefs = SharedPrefAuthHelper(requireContext())

        loadSummary()
        loadUsername()
        setupIncomeExpenseChart()

        // Profile icon click opens ProfileActivity
        val profileIcon = view.findViewById<View>(R.id.imageView)
        profileIcon.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        return view
    }

    private fun loadSummary() {
        val transactions = sharedPrefs.getTransactions().orEmpty()

        val totalIncome = transactions.filter { it.type == "Income" }
            .sumOf { it.amount.toDouble() }

        val totalExpense = transactions.filter { it.type == "Expense" }
            .sumOf { it.amount.toDouble() }

        val balance = totalIncome - totalExpense

        tvIncome?.text = "Total Income: Rs. %.2f".format(totalIncome)
        tvExpense?.text = "Total Expense: Rs. %.2f".format(totalExpense)
        tvBalance?.text = "Balance: Rs. %.2f".format(balance)
    }

    private fun loadUsername() {
        val username = authPrefs.getUsername() ?: "Guest"
        tvUsername?.text = "Welcome, $username"
    }

    private fun setupIncomeExpenseChart() {
        val transactions = sharedPrefs.getTransactions().orEmpty()

        val totalIncome = transactions.filter { it.type == "Income" }
            .sumOf { it.amount.toDouble() }

        val totalExpense = transactions.filter { it.type == "Expense" }
            .sumOf { it.amount.toDouble() }

        // Prepare entries for the BarChart
        val incomeEntry = BarEntry(0f, totalIncome.toFloat())
        val expenseEntry = BarEntry(1f, totalExpense.toFloat())

        val entries = listOf(incomeEntry, expenseEntry)

        // Create BarDataSet for income and expense
        val barDataSet = BarDataSet(entries, "Income vs Expense")

        // Set the colors for income (green) and expense (red)
        barDataSet.setColors(Color.GREEN, Color.RED)

        // Create BarData object
        val barData = BarData(barDataSet)

        // Set data to the BarChart
        barChart.data = barData
        barChart.invalidate()  // Refresh the chart
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tvIncome = null
        tvExpense = null
        tvBalance = null
        tvUsername = null
    }
}
