package com.example.chattapp.realm

import com.example.chattapp.models.Contact
import com.example.chattapp.models.User
import io.realm.Realm

class UserDao {

    val db = Realm.getDefaultInstance()

    fun getUsers(): ArrayList<User> {

        val userList = ArrayList<User>()

        userList.addAll(db.where(User::class.java).findAllAsync())

        return userList

    }

    fun addUser(first: String, last: String, username: String, mail: String, pw: String) {

        db.executeTransactionAsync {

            val newUser = User().apply {
                name = first
                lastName = last
                userName = username
                eMail = mail
                password = pw
            }
            it.insert(newUser)

        }

    }

    fun deleteUser(userId: String){

        db.executeTransaction {

            val user = db.where(Contact::class.java).equalTo("id", userId).findFirstAsync()
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

    fun checkPassword(user: String, password: String): Boolean {
        var rightPw = false
        db.executeTransaction {
            val user = db.where(User::class.java).equalTo("userName", user).findFirst()
            if (user?.password == password) {
                rightPw = true
            }
        }
        return rightPw
    }
}