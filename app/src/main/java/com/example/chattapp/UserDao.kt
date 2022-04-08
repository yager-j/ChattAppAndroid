package com.example.chattapp

import android.util.Log
import io.realm.Realm


class UserDao {

    val db: Realm = Realm.getDefaultInstance()

    fun getUsers(): ArrayList<User> {
        val userList = ArrayList<User>()
        userList.addAll(db.where(User::class.java).findAllAsync())
        return userList
    }

    fun addUser(first: String, last: String, username: String, mail: String, pw: String, login: Boolean) {
        db.executeTransactionAsync {
            val newUser = User().apply {
                name = first
                lastName = last
                userName = username
                eMail = mail
                password = pw
                loggedIn = login
            }
            it.insert(newUser)
        }
    }

    fun deleteUser(userId: String){
        db.executeTransaction {
            val user = db.where(User::class.java).equalTo("id", userId).findFirstAsync()
            user?.deleteFromRealm()
        }
    }

    fun checkIfUserExists(username: String): Boolean {
        var exists = true
        db.executeTransaction {
            if (username.contains("@")) {
                val user = db.where(User::class.java).equalTo("eMail", username).findFirst()
                if (user == null) {
                    exists = false
                }
            } else {
                val user = db.where(User::class.java).equalTo("userName", username).findFirst()
                if (user == null) {
                    exists = false
                }
            }
        }
        return exists
    }

    fun checkPassword(input: String, password: String): Boolean {
        var rightPw = false
        db.executeTransaction {
            val user = if (input.contains("@")) {
                db.where(User::class.java).equalTo("eMail", input).findFirst()
            } else {
                db.where(User::class.java).equalTo("userName", input).findFirst()
            }
            if (user?.password == password) {
                rightPw = true
            }
        }
        return rightPw
    }

    fun logInUser(userOrMail : String) {
        db.executeTransaction {
            val user = if (userOrMail.contains("@")) {
                db.where(User::class.java).equalTo("eMail", userOrMail).findFirst()
            } else {
                db.where(User::class.java).equalTo("userName", userOrMail).findFirst()
            }
            user?.loggedIn = true
        }
    }

    fun logOutUser() {
        db.executeTransaction {
            val user = db.where(User::class.java).equalTo("loggedIn", true).findFirst()
            user?.loggedIn = false
        }
    }

    fun displayCurrentUser() : String {
        val user = db.where(User::class.java).equalTo("loggedIn", true).findFirst()
        return user?.userName ?: ""
    }

    fun debugShowUser(username : String){
        db.executeTransaction {
            val user = db.where(User::class.java).equalTo("userName", username).findFirst()
            Log.d("LoginUser", "user:$user")
        }
    }
}