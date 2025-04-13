package com.example.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(3000)

            val isFirst = SharedPrefUtil.isFirstLaunch(this@SplashActivity)
            val intent = if (!isFirst) {
                Intent(this@SplashActivity, OnboardingActivity::class.java)
            } else {
                Intent(this@SplashActivity, MainActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}
