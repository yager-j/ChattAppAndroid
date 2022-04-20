package com.example.chattapp.firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.example.chattapp.NewChatActivity
import com.example.chattapp.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirestoreUserDao {

    private val ID_KEY = "id"
    private val USERNAME_KEY = "username"
    private val EMAIL_KEY = "email"
    private val PASSWORD_KEY = "password"

    private val USERS_COLLECTION = "users"

    private val firebaseDB = FirebaseFirestore.getInstance()

    constructor(activity: NewChatActivity){
        val userList = ArrayList<User>()

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

    constructor(){

    }

    fun addUser(first: String, last: String, username: String, mail: String, pw: String) {

    }

    fun checkIfUserExists(userOrMail: String) : Boolean {
        val query: Query = if (userOrMail.contains("@"))
            firebaseDB.collection(USERS_COLLECTION).whereEqualTo(EMAIL_KEY, userOrMail)
        else
            firebaseDB.collection(USERS_COLLECTION).whereEqualTo(USERNAME_KEY, userOrMail)

        return query.get() != null
    }

    fun checkPassword(userOrMail: String, inputPassword: String) : Boolean {
        val query: Query = if (userOrMail.contains("@"))
            firebaseDB.collection(USERS_COLLECTION).whereEqualTo(EMAIL_KEY, userOrMail)
        else
            firebaseDB.collection(USERS_COLLECTION).whereEqualTo(USERNAME_KEY, userOrMail)

        var userPassword = ""
        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d("login", "${document.id} => ${document.data}")
                userPassword = document.data[PASSWORD_KEY].toString()
            }
        }

        Log.d("login", "............................input: $inputPassword, actual: $userPassword")
        //print("............................input: $inputPassword, actual: $userPassword")
        return userPassword == inputPassword
    }
}