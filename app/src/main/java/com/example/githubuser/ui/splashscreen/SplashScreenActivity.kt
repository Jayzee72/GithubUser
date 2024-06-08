package com.example.githubuser.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.R
import com.example.githubuser.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            goToMainActivity()
        }, 1500)

    }

    private fun goToMainActivity() {
        Intent(this, MainActivity::class.java).also{
            startActivity(it)
            finish()
        }
    }
}