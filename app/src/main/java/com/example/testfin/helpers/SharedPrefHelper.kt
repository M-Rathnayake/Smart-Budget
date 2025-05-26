package com.example.testfin.helpers

import android.content.Context
import android.content.SharedPreferences
import com.example.testfin.data.Transaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val defaultIncomeCategories = listOf(
        "Salary", "Gift", "Investment", "Savings income", "Other"
    )

    private val defaultExpenseCategories = listOf(
        "Food", "Transport", "Bills", "Entertainment", "Shopping", "Health", "Loan interest", "Other"
    )

    // Initialize default categories if not saved already
    fun initDefaultCategories() {
        if (!prefs.contains("categories_income")) {
            prefs.edit().putStringSet("categories_income", defaultIncomeCategories.toSet()).apply()
        }
        if (!prefs.contains("categories_expense")) {
            prefs.edit().putStringSet("categories_expense", defaultExpenseCategories.toSet()).apply()
        }
    }

    // Get income categories from SharedPreferences
    fun getIncomeCategories(): List<String> {
        return prefs.getStringSet("categories_income", defaultIncomeCategories.toSet())?.toList() ?: defaultIncomeCategories
    }

    // Get expense categories from SharedPreferences
    fun getExpenseCategories(): List<String> {
        return prefs.getStringSet("categories_expense", defaultExpenseCategories.toSet())?.toList() ?: defaultExpenseCategories
    }

    // Save income categories to SharedPreferences
    fun saveIncomeCategories(categories: List<String>) {
        prefs.edit().putStringSet("categories_income", categories.toSet()).apply()
    }

    // Save expense categories to SharedPreferences
    fun saveExpenseCategories(categories: List<String>) {
        prefs.edit().putStringSet("categories_expense", categories.toSet()).apply()
    }

    // Save transactions to SharedPreferences
    fun saveTransactions(transactions: List<Transaction>) {
        val json = gson.toJson(transactions)
        prefs.edit().putString("transactions", json).apply()
    }

    // Get transactions from SharedPreferences
    fun getTransactions(): List<Transaction> {
        val json = prefs.getString("transactions", "[]") ?: "[]"
        val type = object : TypeToken<List<Transaction>>() {}.type
        return gson.fromJson(json, type)
    }

}
