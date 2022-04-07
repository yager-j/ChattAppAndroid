package com.example.chattapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class FirestoreContactDao {

    private val ID_KEY = "id"
    private val USER_NAME_KEY = "user_name"
    private val E_MAIL_KEY = "e_mail"
    private val USERS_COLLECTION = "contacts"

    private val firebaseDB = FirebaseFirestore.getInstance()

    fun saveContact(contact: Contact) {

        val list = HashMap<String, Any>()

        list[ID_KEY] = contact.id
        list[USER_NAME_KEY] = contact.userName
        list[E_MAIL_KEY] = contact.eMail

        firebaseDB
            .collection(USERS_COLLECTION)
            .document(contact.id)
            .set(list)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User added successfully")
            }
            .addOnFailureListener {
                Log.d(ContentValues.TAG, "Failed to add User")
            }


    }

    fun deleteContact(userId: String) {

        firebaseDB
            .document("$USERS_COLLECTION/$userId")
            .delete()
            .addOnSuccessListener {
                Log.w(ContentValues.TAG, "User with id: $userId delete successfully")
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Failed to delete user with id: $userId")
            }

    }

    fun findUser(searchTerm: String, contactDao: ContactDao) {

        var user = Contact()

        firebaseDB
            .collection(USERS_COLLECTION)
            .get()
            .addOnSuccessListener { result ->

                if (result != null) {
                    
                    for (doc in result) {

                        var loadedId = doc.getString(ID_KEY)
                        val loadedName = doc.getString(USER_NAME_KEY)
                        val loadedMail = doc.getString(E_MAIL_KEY)

                        //backup for later
                        //|| searchTerm == loadedMail
                        if (searchTerm == loadedName) {

                            user.id = loadedId!!
                            user.userName = loadedName!!
                            user.eMail = loadedMail!!

                            contactDao.addContact(user)
                            saveContact(user)

                        }
                    }
                }

            }
            .addOnFailureListener {
                Log.d(TAG, "findUser: FAILED MISERABLY")
            }
    }

    fun saveMessage(message: Message){

        val messageHashMap = hashMapOf(
            "id" to message.id,
            "sender" to message.sender,
            "receiver" to message.receiver,
            "text" to message.text,
            "timestamp" to message.timestamp
        )

        firebaseDB
            .collection(USERS_COLLECTION)
            .document(message.receiver)
            .collection("messages")
            .document(message.id)
            .set(messageHashMap)
            .addOnSuccessListener { Log.d("FIRESTORE", "Message sent to Firestore") }
            .addOnFailureListener { Log.d("FIRESTORE", "Message failed to send") }
    }

    fun loadMessages(activity: ChatActivity, contact: Contact) {
        var messagesList = ArrayList<Message>()

        firebaseDB
            .collection(USERS_COLLECTION)
            .document(contact.id)
            .collection("messages")
            .get()
            .addOnSuccessListener { result ->
                for(doc in result) {

                    val msg = Message()

                    val loadedId = doc.getString("id")
                    val loadedSender = doc.getString("sender")
                    val loadedReceiver = doc.getString("receiver")
                    val loadedText = doc.getString("text")
                    val loadedTimestamp = doc.getDate("timestamp")

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