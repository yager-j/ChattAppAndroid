package com.example.chattapp.realm

import com.example.chattapp.models.Contact
import com.example.chattapp.models.User
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import io.realm.Realm

class UserDao {

    val USERS_COLLECTIONS = "users"
    val USERNAME_KEY = "username"
    val EMAIL_KEY = "email"

    val db: Realm = Realm.getDefaultInstance()
    val fb : FirebaseFirestore = FirebaseFirestore.getInstance()
    //val ref : DatabaseReference = fb.collection(USERS_COLLECTIONS).document("id")


    fun getUsers(): ArrayList<User> {

        val userList = ArrayList<User>()

        userList.addAll(db.where(User::class.java).findAllAsync())

        return userList

    }

    fun addUser(first: String, last: String, user: String, mail: String, pw: String, login: Boolean) {
        db.executeTransactionAsync {

            val newUser = User().apply {
                first_name = first
                last_name = last
                username = user
                email = mail
                password = pw
                loggedIn = login
            }
            it.insert(newUser)
        }
    }

    fun addUserToFirebase(first: String, last: String, user: String, mail: String, pw: String) {
        val newUser = User().apply{
            first_name = first
            last_name = last
            username = user
            email = mail
            password = pw
        }
        val hashUser = HashMap<String, String>()
        hashUser.put("id", newUser.id)
        hashUser.put("first_name", newUser.first_name)
        hashUser.put("last_name", newUser.last_name)
        hashUser.put("username", newUser.username)
        hashUser.put("email", newUser.email)
        hashUser.put("password", newUser.password)
        fb.document("$USERS_COLLECTIONS/${newUser.id}").set(hashUser)
    }

    fun deleteUser(userId: String) {
        db.executeTransaction {
            val user = db.where(User::class.java).equalTo("id", userId).findFirstAsync()
            user?.deleteFromRealm()
        }
    }

    fun checkIfUserExists(userOrMail: String): Boolean {
        val user = if (userOrMail.contains("@")) {
            db.where(User::class.java).equalTo(EMAIL_KEY, userOrMail).findFirst()
        } else {
            db.where(User::class.java).equalTo(USERNAME_KEY, userOrMail).findFirst()
        }
        return user != null
    }

    fun checkPassword(userOrMail: String, password: String): Boolean {
        val user = if (userOrMail.contains("@")) {
            db.where(User::class.java).equalTo(EMAIL_KEY, userOrMail).findFirst()
        } else {
            db.where(User::class.java).equalTo(USERNAME_KEY, userOrMail).findFirst()
        }
        return user?.password == password
    }

    fun logInUser(userOrMail : String) {
        val user = if (userOrMail.contains("@")) {
            db.where(User::class.java).equalTo(EMAIL_KEY, userOrMail).findFirst()
        } else {
            db.where(User::class.java).equalTo(USERNAME_KEY, userOrMail).findFirst()
        }
        user?.loggedIn = true
    }

    fun logOutUser() {
        val user = db.where(User::class.java).equalTo("loggedIn", true).findFirst()
        user?.loggedIn = false
    }

    fun displayCurrentUser() : String {
        val user = db.where(User::class.java).equalTo("loggedIn", true).findFirst()
        return user?.username ?: ""
    }

    fun debugShowUser(username : String){
        val user = db.where(User::class.java).equalTo(USERNAME_KEY, username).findFirst()
        Log.d("LoginUser", "user:$user")
    }
}