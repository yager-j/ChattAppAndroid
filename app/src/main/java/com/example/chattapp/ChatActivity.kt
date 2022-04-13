package com.example.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chattapp.databinding.ActivityChatBinding
import com.example.chattapp.firebase.FirestoreChatDao
import com.example.chattapp.firebase.FirestoreMessageDao
import com.example.chattapp.models.Chat
import com.example.chattapp.models.Message

private lateinit var binder: ActivityChatBinding
private lateinit var firestoreMessageDao: FirestoreMessageDao
private lateinit var firestoreChatDao: FirestoreChatDao


class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        var id = intent.getStringExtra("chatID")

        firestoreMessageDao = if(id != null) {
            FirestoreMessageDao(this, id)
        } else {
            FirestoreMessageDao()
        }

        firestoreChatDao = FirestoreChatDao()

        //Send messages
        binder.send.setOnClickListener {
            val messages = binder.textView.text.toString()
            val newMessage = binder.textInput.text.toString()

            binder.textView.text = messages + newMessage + "\n"
            binder.textInput.text.clear()

            //Create new chat
            if(id == null){
                val chat = Chat()
                id = chat.id
                chat.usersInChat.add("dave")
                chat.usersInChat.addAll(intent.getStringArrayListExtra("userList") as ArrayList<String>)
                firestoreChatDao.saveChat(chat)
            }
            createMessage(newMessage, id!!)
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