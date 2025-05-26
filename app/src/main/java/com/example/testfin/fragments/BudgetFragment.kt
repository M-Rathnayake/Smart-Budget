package com.example.testfin.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.testfin.R
import com.example.testfin.helpers.SharedPrefHelper

class BudgetFragment : Fragment() {

    private lateinit var etBudgetAmount: EditText
    private lateinit var btnSaveBudget: Button
    private lateinit var progressBudget: ProgressBar
    private lateinit var tvBudgetStatus: TextView
    private lateinit var tvBudgetWarning: TextView

    private val CHANNEL_ID = "budget_channel"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_budget, container, false)

        requestNotificationPermission()

        etBudgetAmount = rootView.findViewById(R.id.etBudgetAmount)
        btnSaveBudget = rootView.findViewById(R.id.btnSaveBudget)
        progressBudget = rootView.findViewById(R.id.progressBudget)
        tvBudgetStatus = rootView.findViewById(R.id.tvBudgetStatus)
        tvBudgetWarning = rootView.findViewById(R.id.tvBudgetWarning)

        // Create notification channel
        createNotificationChannel()

        // Load saved budget
        val savedBudget = getSavedBudgetAmount()
        if (savedBudget > 0) {
            updateProgress(savedBudget, getTotalSpentAmount())
        }

        btnSaveBudget.setOnClickListener {
            val budgetAmount = etBudgetAmount.text.toString().toDoubleOrNull()
            if (budgetAmount == null || budgetAmount <= 0) {
                Toast.makeText(context, "Please enter a valid budget amount", Toast.LENGTH_SHORT).show()
            } else {
                saveBudgetAmount(budgetAmount)
                updateProgress(budgetAmount, getTotalSpentAmount())
            }
        }

        return rootView
    }

    private fun saveBudgetAmount(amount: Double) {
        val sharedPreferences = context?.getSharedPreferences("budget_pref", Context.MODE_PRIVATE)
        sharedPreferences?.edit()?.putFloat("monthly_budget", amount.toFloat())?.apply()
    }

    private fun getSavedBudgetAmount(): Double {
        val sharedPreferences = context?.getSharedPreferences("budget_pref", Context.MODE_PRIVATE)
        return sharedPreferences?.getFloat("monthly_budget", 0f)?.toDouble() ?: 0.0
    }

    private fun getTotalSpentAmount(): Double {
        val sharedPrefs = SharedPrefHelper(requireContext())
        val transactions = sharedPrefs.getTransactions() ?: emptyList()

        return transactions
            .filter { it.type == "Expense" }
            .sumOf { it.amount.toDouble() }
    }


    private fun updateProgress(budgetAmount: Double, totalSpent: Double) {
        val progress = (totalSpent / budgetAmount) * 100
        progressBudget.progress = progress.toInt()

        val currencyFormat = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("en", "LK"))
        tvBudgetStatus.text = "${currencyFormat.format(totalSpent)} of ${currencyFormat.format(budgetAmount)} spent"

        when {
            progress >= 100 -> {
                tvBudgetWarning.text = "Warning: Budget exceeded!"
                tvBudgetWarning.setTextColor(resources.getColor(R.color.expenseColor))
                tvBudgetWarning.visibility = View.VISIBLE
                showBudgetNotification("You've exceeded your monthly budget!")
            }
            progress >= 80 -> {
                tvBudgetWarning.text = "Warning: You're nearing your budget!"
                tvBudgetWarning.setTextColor(resources.getColor(R.color.warningColor))
                tvBudgetWarning.visibility = View.VISIBLE
                showBudgetNotification("You're close to your monthly budget!")
            }
            else -> {
                tvBudgetWarning.visibility = View.GONE
            }
        }
    }

    private fun showBudgetNotification(message: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.budget)
                .setContentTitle("Budget Alert")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            with(NotificationManagerCompat.from(requireContext())) {
                notify(1001, builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Budget Alerts"
            val descriptionText = "Notifications for budget limit alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please enable notifications for budget alerts", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
