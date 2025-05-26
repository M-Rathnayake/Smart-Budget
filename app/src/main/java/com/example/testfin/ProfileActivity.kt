package com.example.testfin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.testfin.helpers.SharedPrefAuthHelper

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile) // Make sure the layout filename is `profile.xml`

        val authHelper = SharedPrefAuthHelper(this)
        val username = authHelper.getUsername() ?: "User"

        // Set the username to all relevant views
        val profileNameTextView = findViewById<TextView>(R.id.tvProfileName)
        val usernameTextView = findViewById<TextView>(R.id.tvUsername)

        // Populate the views (you can later improve this with more info from SharedPrefs if needed)
        profileNameTextView.text = username
        usernameTextView.text = username

    }
}
