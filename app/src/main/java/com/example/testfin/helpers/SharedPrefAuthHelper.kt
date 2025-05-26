package com.example.testfin.helpers

import android.content.Context
import android.content.SharedPreferences

class SharedPrefAuthHelper(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    // Save the user's credentials (username and password)
    fun saveUser(username: String, password: String) {
        val editor = prefs.edit()
        editor.putString("username", username)
        editor.putString("password", password) // You should ideally encrypt this!
        editor.apply()
    }

    // Check if the entered username and password match the saved ones
    fun isValidUser(username: String, password: String): Boolean {
        val savedUsername = prefs.getString("username", null)
        val savedPassword = prefs.getString("password", null)
        return username == savedUsername && password == savedPassword
    }


    // Save the username to indicate a logged-in state
    fun saveUsername(username: String) {
        val editor = prefs.edit()
        editor.putString("currentUser", username)
        editor.apply()
    }

    // Get the logged-in user's username
    fun getUsername(): String? {
        return prefs.getString("currentUser", null)
    }

    // Log out the user by clearing saved session data
    fun logOut() {
        val editor = prefs.edit()
        editor.remove("currentUser")
        editor.apply()
    }

    // Check if the user is logged in (i.e., if there is a current user)
    fun isUserLoggedIn(): Boolean {
        return prefs.contains("currentUser")
    }

    fun isLoggedIn(): Boolean {
        return prefs.contains("username")
    }
}
