package com.example.shopit.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shopit.R
import com.example.shopit.ui.login.LoginActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Thread{
            Thread.sleep(1000)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }.start()
    }

    companion object {
        private const val TAG = "EliteControl-Splash"
    }
}