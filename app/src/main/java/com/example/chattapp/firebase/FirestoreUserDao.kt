package com.example.chattapp.firebase

import com.example.chattapp.NewChatActivity
import com.example.chattapp.models.User
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreUserDao {

    private val ID_KEY = "id"
    private val USERNAME_KEY = "username"
    private val EMAIL_KEY = "email"

    private val USERS_COLLECTION = "users"

    private val firebaseDB = FirebaseFirestore.getInstance()

    var userList = ArrayList<User>()

    fun loadUsers(){
        userList.clear()

        firebaseDB
            .collection(USERS_COLLECTION)
            .get().addOnSuccessListener { result ->
                if (result != null) {
                    userList.clear()
                    for (doc in result) {
                        val user = User()
                        user.id = doc.getString(ID_KEY)!!
                        user.userName = doc.getString(USERNAME_KEY)!!
                        user.eMail = doc.getString(EMAIL_KEY)!!

                        userList.add(user)
                    }
                }
            }
    }

    fun firestoreUserListener(activity: NewChatActivity){
        userList.clear()

        firebaseDB
            .collection(USERS_COLLECTION)
            .addSnapshotListener { result, error ->
                if (result != null) {
                    userList.clear()
                    for (doc in result) {
                        val user = User()
                        user.id = doc.getString(ID_KEY)!!
                        user.userName = doc.getString(USERNAME_KEY)!!
                        user.eMail = doc.getString(EMAIL_KEY)!!

                        userList.add(user)
                    }
                    activity.showUsers(userList)
                }
            }
    }

    fun getUsername(id: String): String{
        println(userList.toString())
        for(user in userList){
            //println("sender:$id")
            //println("user:${user.toString()}")
            if(id == user.id)
                return user.userName
        }
        return "No Username"
    }
}