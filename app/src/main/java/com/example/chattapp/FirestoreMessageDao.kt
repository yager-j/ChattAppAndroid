package com.example.chattapp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreMessageDao {

    private val ID_KEY = "id"
    private val SENDER_KEY = "sender"
    private val RECEIVER_KEY = "receiver"
    private val TEXT_KEY = "text"
    private val TIMESTAMP_KEY = "timestamp"

    private val MESSAGES_COLLECTION = "messages"
    private val USERS_COLLECTION = "contacts"

    private val firebaseDB = FirebaseFirestore.getInstance()

    fun saveMessage(message: Message){

        val messageHashMap = hashMapOf(
            ID_KEY to message.id,
            SENDER_KEY to message.sender,
            RECEIVER_KEY to message.receiver,
            TEXT_KEY  to message.text,
            TIMESTAMP_KEY to message.timestamp
        )

        firebaseDB
            .collection(USERS_COLLECTION)
            .document(message.receiver)
            .collection(MESSAGES_COLLECTION)
            .document(message.id)
            .set(messageHashMap)
            .addOnSuccessListener { Log.d("FIRESTORE", "Message sent to Firestore") }
            .addOnFailureListener { Log.d("FIRESTORE", "Message failed to send") }
    }

    fun loadMessages(activity: ChatActivity, contact: Contact) {
        val messagesList = ArrayList<Message>()

        firebaseDB
            .collection(USERS_COLLECTION)
            .document(contact.id)
            .collection(MESSAGES_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                for(doc in result) {

                    val msg = Message()

                    val loadedId = doc.getString(ID_KEY)
                    val loadedSender = doc.getString(SENDER_KEY)
                    val loadedReceiver = doc.getString(RECEIVER_KEY)
                    val loadedText = doc.getString(TEXT_KEY)
                    val loadedTimestamp = doc.getDate(TIMESTAMP_KEY)

                    msg.id = loadedId!!
                    msg.sender = loadedSender!!
                    msg.receiver = loadedReceiver!!
                    msg.text = loadedText!!
                    msg.timestamp = loadedTimestamp!!

                    messagesList.add(msg)
                }
                activity.showMessages(messagesList)
            }
            .addOnFailureListener {
                Log.d("FIRESTORE", "Failed to load messages")
            }
            .addOnCanceledListener {
                Log.d("FIRESTORE", "Load messages canceled")
            }
    }
}