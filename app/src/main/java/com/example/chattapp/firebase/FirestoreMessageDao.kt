package com.example.chattapp.firebase

import android.util.Log
import com.example.chattapp.ChatActivity
import com.example.chattapp.models.Message
import com.example.chattapp.realm.MessageDao
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreMessageDao {

    private val ID_KEY = "id"
    private val SENDER_KEY = "sender"
    private val TEXT_KEY = "text"
    private val TIMESTAMP_KEY = "timestamp"
    private val REFERENCE_CHAT_ID = "referenceChatId"

    private val LAST_MESSAGE_KEY = "last_message"

    private val MESSAGES_COLLECTION = "messages"
    private val CHATS_COLLECTION = "chats"


    private val firebaseDB = FirebaseFirestore.getInstance()

    constructor()

    constructor(activity: ChatActivity, id: String){
        firebaseDB
            .collection(CHATS_COLLECTION)
            .document(id)
            .collection(MESSAGES_COLLECTION)
            .orderBy(TIMESTAMP_KEY)
            .addSnapshotListener(activity) { result, error ->
                val messagesList = ArrayList<Message>()
                if (result != null) {
                    for(doc in result) {

                        val msg = Message()

                        val loadedId = doc.getString(ID_KEY)
                        val loadedSender = doc.getString(SENDER_KEY)
                        val loadedText = doc.getString(TEXT_KEY)
                        val loadedTimestamp = doc.getDate(TIMESTAMP_KEY)
                        val loadedReferenceId = doc.getString(REFERENCE_CHAT_ID)

                        msg.id = loadedId!!
                        msg.sender = loadedSender!!
                        msg.text = loadedText!!
                        msg.timestamp = loadedTimestamp!!
                        msg.referenceChatId = loadedReferenceId!!

                        messagesList.add(msg)
                        MessageDao.insertMessage(msg)
                    }
                    //activity.showMessages(messagesList)
                    activity.loadMessages(messagesList)
                    Log.d("FIRESTORE", "Messages updated")
                }

            }
    }

    fun saveMessage(message: Message, chatID: String){

        val messageHashMap = hashMapOf(
            ID_KEY to message.id,
            SENDER_KEY to message.sender,
            TEXT_KEY  to message.text,
            TIMESTAMP_KEY to message.timestamp,
            REFERENCE_CHAT_ID to chatID
        )

        firebaseDB
            .collection(CHATS_COLLECTION)
            .document(chatID)
            .collection(MESSAGES_COLLECTION)
            .document(message.id)
            .set(messageHashMap)
            .addOnSuccessListener { Log.d("FIRESTORE", "Message sent to Firestore") }
            .addOnFailureListener { Log.d("FIRESTORE", "Message failed to send") }

        firebaseDB.collection(CHATS_COLLECTION).document(chatID).update(LAST_MESSAGE_KEY, message.text)
        firebaseDB.collection(CHATS_COLLECTION).document(chatID).update(TIMESTAMP_KEY, message.timestamp)

    }

    fun loadMessages(activity: ChatActivity, id: String) {
        val messagesList = ArrayList<Message>()

        firebaseDB
            .collection(CHATS_COLLECTION)
            .document(id)
            .collection(MESSAGES_COLLECTION)
            .orderBy(TIMESTAMP_KEY)
            .get()
            .addOnSuccessListener { result ->
                for(doc in result) {

                    val msg = Message()

                    val loadedId = doc.getString(ID_KEY)
                    val loadedSender = doc.getString(SENDER_KEY)
                    val loadedText = doc.getString(TEXT_KEY)
                    val loadedTimestamp = doc.getDate(TIMESTAMP_KEY)

                    msg.id = loadedId!!
                    msg.sender = loadedSender!!
                    msg.text = loadedText!!
                    msg.timestamp = loadedTimestamp!!

                    messagesList.add(msg)
                }
                activity.loadMessages(messagesList)
            }
            .addOnFailureListener {
                Log.d("FIRESTORE", "Failed to load messages")
            }
            .addOnCanceledListener {
                Log.d("FIRESTORE", "Load messages canceled")
            }
    }
}