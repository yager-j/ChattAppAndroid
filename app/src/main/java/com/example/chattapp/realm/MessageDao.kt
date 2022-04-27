package com.example.chattapp.realm

import com.example.chattapp.models.Message
import io.realm.Realm
import java.util.ArrayList

object MessageDao {

    val realm = Realm.getDefaultInstance()

    fun insertMessage(message: Message){
        realm.executeTransaction {
            it.insertOrUpdate(message)
        }
    }

    fun getMessages(chatId : String) : ArrayList<Message> {
        val messageList = ArrayList<Message>()

        realm.executeTransaction {
            val messages = it.where(Message::class.java).equalTo("referenceChatId", chatId).findAll()
            messageList.addAll(messages)
        }
        return messageList
    }
}