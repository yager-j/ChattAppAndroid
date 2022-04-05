package com.example.chattapp

import io.realm.Realm

class UserDao {

    val db = Realm.getDefaultInstance()

    fun getUsers(): ArrayList<User> {

        val userList = ArrayList<User>()

        userList.addAll(db.where(User::class.java).findAllAsync())

        return userList

    }

    fun addUser() {

        db.executeTransactionAsync {

            val contact = User()
            it.insert(contact)

        }

    }
}