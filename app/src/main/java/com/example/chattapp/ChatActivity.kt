package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityChatBinding
import com.example.chattapp.firebase.FirestoreChatDao
import com.example.chattapp.firebase.FirestoreMessageDao
import com.example.chattapp.models.Chat
import com.example.chattapp.models.Message
import com.example.chattapp.realm.MessageDao

class ChatActivity : AppCompatActivity() {

    private lateinit var binder: ActivityChatBinding
    private lateinit var firestoreMessageDao: FirestoreMessageDao
    private lateinit var firestoreChatDao: FirestoreChatDao
    private var currentUserId = UserManager.currentUser!!.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        var id = intent.getStringExtra("chatID")

        if (id != null) {
            println(MessageDao.getMessages(id))
        }

        binder.chatName.text = intent.getStringExtra("chatName")

        firestoreMessageDao = if(id != null) {
            FirestoreMessageDao(this, id)
        } else {
            FirestoreMessageDao()
        }

        firestoreChatDao = FirestoreChatDao()

        //Send messages
        binder.sendButton.setOnClickListener {

            val newMessage = binder.textInput.text.toString()

            binder.textInput.text.clear()

            //Create new chat
            if(id == null){
                val chat = Chat()
                id = chat.id
                chat.usersInChat.addAll(intent.getStringArrayListExtra("userIdList") as ArrayList<String>)
                val usernameList = intent.getStringArrayListExtra("userNameList")
                val chatName = usernameList.toString()
                chat.chatName = chatName.substring(1, chatName.length - 1)

                firestoreChatDao.saveChat(chat)
                //create listener
                firestoreMessageDao = FirestoreMessageDao(this, id!!)
            }
            createMessage(newMessage, id!!)
        }
    }

    fun loadMessages(messageList: ArrayList<Message>) {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        binder.recyclerMessages.layoutManager = layoutManager
        val adapter = MessageAdapter(this, messageList)
        binder.recyclerMessages.adapter = adapter
    }

    private fun createMessage(text: String, chatID: String){
        val msg = Message(text = text, sender = currentUserId)
        firestoreMessageDao.saveMessage(msg, chatID)
    }

    @Override
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}