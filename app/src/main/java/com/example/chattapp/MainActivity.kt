package com.example.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chattapp.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmConfiguration

private lateinit var binder: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("chatAppDB")
            .allowWritesOnUiThread(true)
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}