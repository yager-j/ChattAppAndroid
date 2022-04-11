package com.example.chattapp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreChatDao {

    private val ID_KEY = "id"
    private val USERS_IN_CHAT_KEY = "users_in_chat"

    private val CHATS_COLLECTION = "chats"

    private val firebaseDB = FirebaseFirestore.getInstance()

    constructor()

    constructor(activity: MainActivity) {
        firebaseDB.collection(CHATS_COLLECTION).addSnapshotListener(activity) { value, error ->
            if (error != null){
                Log.e("FIRESTORE", "Failed to listen for chats")
            }
            if(value != null) {
                val chatList = ArrayList<Chat>()
                for(doc in value) {
                    val chat = Chat()

                    chat.id = doc.getString(ID_KEY)!!
                    chat.usersInChat = doc.get(USERS_IN_CHAT_KEY) as ArrayList<String>

                    chatList.add(chat)
                }
                activity.loadList(chatList)
                println(chatList.toString())
                Log.d("FIRESTORE", "Updated Chat List")
            }
        }
    }
}