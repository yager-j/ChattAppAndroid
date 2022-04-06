package com.example.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chattapp.databinding.ActivityChatBinding

private lateinit var binder: ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binder.root)



        binder.send.setOnClickListener {
            var messages = binder.textView.text.toString()
            val input = binder.textInput.text.toString()

            binder.textView.text = messages + input + "\n"

            binder.textInput.text.clear()
        }
    }
}