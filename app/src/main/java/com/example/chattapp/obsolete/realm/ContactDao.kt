package com.example.chattapp

import com.example.chattapp.models.Contact
import io.realm.Realm
import java.util.ArrayList

class ContactDao {

//    val db = Realm.getDefaultInstance()
//
//    fun getContacts(): ArrayList<Contact> {
//        val userList = ArrayList<Contact>()
//        userList.addAll(db.where(Contact::class.java).findAllAsync())
//        return userList
//    }
//
//    fun addContact(username: String) {
//        db.executeTransactionAsync {
//            val newContact = Contact().apply {
//                userName = username
//            }
//            it.insert(newContact)
//        }
//    }
//
//    fun deleteContact(userId: String) {
//        db.executeTransaction {
//            val user = db.where(Contact::class.java).equalTo("id", userId).findFirstAsync()
//            user?.deleteFromRealm()
//        }
//    }
}