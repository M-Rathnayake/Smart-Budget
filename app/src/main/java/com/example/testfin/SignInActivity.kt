package com.example.testfin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfin.helpers.SharedPrefAuthHelper

class SignInActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button

    private lateinit var authHelper: SharedPrefAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        usernameEditText = findViewById(R.id.edtUsername)
        passwordEditText = findViewById(R.id.edtPassword)
        signInButton = findViewById(R.id.btnSignIn)

        authHelper = SharedPrefAuthHelper(this)

        signInButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else if (authHelper.isValidUser(username, password)) {
                // Save session (store user data for later use)
                authHelper.saveUser(username, password)

                // Show success toast
                Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show()

                // Redirect to MainActivity after successful sign-in
                val intent = Intent(this, MainActivity::class.java)

                // Use flags to prevent going back to SignInActivity
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                // Start MainActivity and finish SignInActivity
                startActivity(intent)
                finish() // Call finish() to close the SignInActivity and remove it from the stack
            } else {
                // Invalid credentials
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Redirect to SignUp activity (optional, based on your app's flow)
    fun onSignUpRedirect(view: android.view.View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}

