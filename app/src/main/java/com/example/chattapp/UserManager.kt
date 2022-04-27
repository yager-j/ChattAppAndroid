package com.example.chattapp

import android.util.Log
import com.example.chattapp.models.User
import io.realm.Realm


object UserManager {

    var currentUser: User? = null

    private val realmDB: Realm = Realm.getDefaultInstance()

    fun saveLoggedInUser(user: User) {
        realmDB.executeTransaction {
            val newUser = User().apply {
                id = user.id
                first_name = user.first_name
                last_name = user.last_name
                username = user.username
                email = user.email
                password = user.password
            }
            it.insert(newUser)
        }
    }

    fun logInUser() {
        realmDB.executeTransaction {
            val user = realmDB.where(User::class.java).findFirst()
            currentUser = user
            Log.d("login", "............................Currently logged in: $user")
        }
    }

    fun logOutUser(userId: String) {
        currentUser = null
        realmDB.executeTransaction {
            val user = realmDB.where(User::class.java).equalTo("id", userId).findFirst()
            user?.deleteFromRealm()
        }
    }
}