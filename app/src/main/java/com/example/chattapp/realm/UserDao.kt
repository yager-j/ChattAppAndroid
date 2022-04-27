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
}