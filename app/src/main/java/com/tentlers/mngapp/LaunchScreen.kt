package com.tentlers.mngapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LaunchScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)

//        TODO: change the splash screen method to not use handler() object.
        Handler().postDelayed(Runnable { startActivity(Intent(this, MainActivity::class.java))
            finish() },2000)

    }
}