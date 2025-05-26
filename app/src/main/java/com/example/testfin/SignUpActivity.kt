package com.example.testfin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfin.helpers.SharedPrefAuthHelper

class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signUpButton: Button

    private lateinit var authHelper: SharedPrefAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        // Initialize views
        usernameEditText = findViewById(R.id.edtSignUpUsername)
        passwordEditText = findViewById(R.id.edtSignUpPassword)
        confirmPasswordEditText = findViewById(R.id.edtConfirmPassword)
        signUpButton = findViewById(R.id.btnSignUp)

        // Initialize SharedPrefAuthHelper for saving user data
        authHelper = SharedPrefAuthHelper(this)

        // Set up Sign Up button click listener
        signUpButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            // Validate input fields
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else if (!password.matches(Regex("^[a-zA-Z0-9]+$"))) {
                Toast.makeText(this, "Password must be alphanumeric only", Toast.LENGTH_SHORT).show()
            } else {
                // Save the username and password using SharedPrefAuthHelper
                authHelper.saveUser(username, password)

                Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()

                // Navigate to the Sign In activity
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish() // Prevent user from coming back to SignUp
            }
        }
    }
}
