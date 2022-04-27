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
    private var currentUserId = UserManager.currentUser!!.id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        var id = intent.getStringExtra("chatID")

        //Load messages from Realm
        if (id != null) { loadMessages(MessageDao.getMessages(id)) }

        //Format chat name
        var chatname = intent.getStringExtra("chatName")
        if (chatname != null) { chatname = formatChatName(chatname) }
        binder.chatName.text = chatname

        //If chat exist add listener
        if(id != null) { firestoreMessageDao = FirestoreMessageDao(this, id) }



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

                FirestoreChatDao.saveChat(chat)
                //create listener
                firestoreMessageDao = FirestoreMessageDao(this, id!!)
            }
            createMessage(newMessage, id!!)
        }
    }

    /**
     * Remove current username from Chat name
     */
    private fun formatChatName(chatname: String):String {
        var chatname = chatname.replace(UserManager.currentUser!!.username, "")
        chatname = chatname.removePrefix(", ")
        chatname = chatname.removeSuffix(", ")
        chatname = chatname.replace(", ,", ", ")
        return chatname
    }

    /**
     * Load message list into recyclerview
     */
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