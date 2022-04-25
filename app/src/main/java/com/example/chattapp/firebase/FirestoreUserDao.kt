package com.example.chattapp.firebase

import android.util.Log
import com.example.chattapp.NewChatActivity
import com.example.chattapp.UserManager
import com.example.chattapp.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.realm.Realm

class FirestoreUserDao {

    private val ID_KEY = "id"
    private val NAME_KEY = "first_name"
    private val LASTNAME_KEY = "last_name"
    private val USERNAME_KEY = "username"
    private val EMAIL_KEY = "email"
    private val PASSWORD_KEY = "password"

    private val USERS_COLLECTION = "users"

    private val firebaseDB = FirebaseFirestore.getInstance()

    constructor(activity: NewChatActivity) {
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

    constructor()

    fun addUser(user: User) {
        val userHashMap = HashMap<String, String>()
        userHashMap[ID_KEY] = user.id
        userHashMap[NAME_KEY] = user.name
        userHashMap[LASTNAME_KEY] = user.lastName
        userHashMap[USERNAME_KEY] = user.userName
        userHashMap[EMAIL_KEY] = user.eMail
        userHashMap[PASSWORD_KEY] = user.password
        firebaseDB.document("$USERS_COLLECTION/${user.id}").set(userHashMap)
    }

    fun userExists(userOrMail: String, callback: (Boolean) -> Unit) {
        val query: Query = if (userOrMail.contains("@"))
            firebaseDB.collection(USERS_COLLECTION).whereEqualTo(EMAIL_KEY, userOrMail)
        else
            firebaseDB.collection(USERS_COLLECTION).whereEqualTo(USERNAME_KEY, userOrMail)

        query.get().addOnSuccessListener {
            if (it.documents.toString() == "[]")
                callback(false)
            else
                callback(true)

            Log.d("login", "........................User Info: ${it.documents}")
        }
    }

    fun checkPassword(userOrMail: String, inputPassword: String, callback: (Boolean) -> Unit) {
        val query: Query = if (userOrMail.contains("@"))
            firebaseDB.collection(USERS_COLLECTION).whereEqualTo(EMAIL_KEY, userOrMail)
        else
            firebaseDB.collection(USERS_COLLECTION).whereEqualTo(USERNAME_KEY, userOrMail)

        //callBack gives an answer (boolean) after it has finished looking through all users
        //if no callback it would check the bool before it had finished and would return false no matter what
        var isCorrect = false
        query.get().addOnSuccessListener { documents ->
            for (document in documents) {
                isCorrect = (document.data[PASSWORD_KEY] == inputPassword)
                //if it is the correct password it adds that user to the "logged in" pool
                //(so far its only one at a time but it could be multiple people logged in at the same time)
                if (isCorrect) {
                    val newUser = User().apply{
                        id = document.data["id"].toString()
                        name = document.data["first_name"].toString()
                        lastName = document.data["last_name"].toString()
                        userName = document.data["username"].toString()
                        eMail = document.data["email"].toString()
                        password = document.data["password"].toString()
                    }
                    saveToManager(newUser)
                }
                Log.d("login", "${document.id} => ${document.data}")
                Log.d("login", "${document.id} => ${document.data[PASSWORD_KEY]}")
            }
            callback(isCorrect)
        }
    }

    fun saveToManager(user: User){
        UserManager.saveLoggedInUser(user)
    }
}