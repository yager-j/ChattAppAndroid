package com.example.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chattapp.databinding.ActivityChatBinding

private lateinit var binder: ActivityChatBinding
private lateinit var firestoreMessageDao: FirestoreMessageDao
private lateinit var firestoreChatDao: FirestoreChatDao


class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        firestoreMessageDao = FirestoreMessageDao()
        firestoreChatDao = FirestoreChatDao()

        val id = intent.getStringExtra("chatID")


        //binder.name.text =

        //Load messages
        if (id != null) {
            firestoreMessageDao.loadMessages(this, id)
        }

        binder.send.setOnClickListener {
            val messages = binder.textView.text.toString()
            val newMessage = binder.textInput.text.toString()

            binder.textView.text = messages + newMessage + "\n"
            binder.textInput.text.clear()

            if (id != null) {
                createMessage(newMessage, id)
            }

        }
    }

    fun showMessages(list: ArrayList<Message>){
        //Show messages
        var messageText = ""
        for(message in list){
            messageText += "${message.text} \n"
        }
        binder.textView.text = messageText
    }

    private fun createMessage(text: String, chatID: String){
        val msg = Message(text = text)
        firestoreMessageDao.saveMessage(msg, chatID)
    }
}