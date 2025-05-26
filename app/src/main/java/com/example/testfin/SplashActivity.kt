package com.example.testfin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // Create this layout for your splash screen
        Log.d("SplashActivity", "SplashActivity started")

        // Delay for 2 seconds, then move to SignInActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignInActivity::class.java)
            Log.d("SplashActivity", "Launching SignInActivity")
            startActivity(intent)
            finish() // Finish SplashActivity so user can't return to it
        }, 2000) // 2000 milliseconds = 2 seconds
    }
}
