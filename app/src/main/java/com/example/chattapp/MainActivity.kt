package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chattapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binder : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binder.root)
        val toLogin = Intent(this, LoginScreen::class.java)

        binder.buttonLogin.setOnClickListener{
            toLogin.putExtra("loginPressed", true)
            startActivity(toLogin)
        }

        binder.buttonRegister.setOnClickListener{
            toLogin.putExtra("loginPressed", false)
            startActivity(toLogin)
        }
    }
}