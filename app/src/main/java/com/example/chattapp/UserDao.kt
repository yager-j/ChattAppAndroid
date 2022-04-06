package com.example.chattapp

import io.realm.Realm

class UserDao {

    val db = Realm.getDefaultInstance()

    fun getUsers(): ArrayList<Contact> {

        val userList = ArrayList<Contact>()

        userList.addAll(db.where(Contact::class.java).findAllAsync())

        return userList

    }

    fun addUser(name: String) {

        db.executeTransactionAsync {

            val contact = Contact().apply {

                userName = name
            }
            it.insert(contact)

        }

    }

    fun deleteUser(userId: String){

        db.executeTransaction {

            val user = db.where(Contact::class.java).equalTo("id", userId).findFirstAsync()
            user?.deleteFromRealm()

        }
    }

}