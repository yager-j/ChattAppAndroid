package com.example.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chattapp.databinding.ActivityChatBinding

private lateinit var binder: ActivityChatBinding
private lateinit var contactDao: ContactDao
private lateinit var firestoreContactDao: FirestoreContactDao
private lateinit var contact: Contact
private lateinit var messageList: ArrayList<Message>

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        firestoreContactDao = FirestoreContactDao()
        contactDao = ContactDao()
        val pos = intent.getIntExtra("pos", 0)
        contact = contactDao.getContacts()[pos]

        binder.name.text = contact.userName

        //Load messages
        firestoreContactDao.loadMessages(this, contact)


        binder.send.setOnClickListener {
            val messages = binder.textView.text.toString()
            val newMessage = binder.textInput.text.toString()

            binder.textView.text = messages + newMessage + "\n"
            binder.textInput.text.clear()

            createMessage(newMessage)

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

    private fun createMessage(text: String){
        val msg = Message(receiver = contact.id, text = text)
        firestoreContactDao.saveMessage(msg)
    }
}