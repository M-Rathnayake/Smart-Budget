package com.example.testfin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testfin.fragments.BackupFragment
import com.example.testfin.fragments.BudgetFragment
import com.example.testfin.fragments.CategoriesFragment
import com.example.testfin.fragments.DashboardFragment
import com.example.testfin.fragments.TransactionsFragment
import com.example.testfin.helpers.SharedPrefAuthHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        bottomNav = findViewById(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> loadFragment(DashboardFragment())
                R.id.nav_categories -> loadFragment(CategoriesFragment())
                R.id.nav_transactions -> loadFragment(TransactionsFragment())
                R.id.nav_budget -> loadFragment(BudgetFragment())
                R.id.nav_backup -> loadFragment(BackupFragment())
            }
            true
        }

        val authHelper = SharedPrefAuthHelper(this)

        // If not logged in, send to SignInActivity
        if (!authHelper.isUserLoggedIn()) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish() // Optional: To prevent user from navigating back to MainActivity without login
        } else {
            // Load Dashboard Fragment if already logged in
            loadFragment(DashboardFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
