package com.example.chattapp.realm

import com.example.chattapp.models.Chat
import com.example.chattapp.models.ChatRealm
import io.realm.Realm
import io.realm.RealmList

object ChatDao {

     fun insertChat(chat: Chat){

        val realm = Realm.getDefaultInstance()

        var realmList = RealmList<String>()
        realmList.addAll(chat.usersInChat)

        realm.executeTransaction {
            val realmChat = ChatRealm(id = chat.id,
                usersInChat = realmList,
                chatName = chat.chatName,
                lastMessage = chat.lastMessage,
                timestamp = chat.timestamp)
            it.insertOrUpdate(realmChat)
        }
    }

    fun getChats() : ArrayList<Chat>{
        val chatList = ArrayList<Chat>()
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val chats = it.where(ChatRealm::class.java).findAll()
            for(chat in chats){
                val newChat = Chat()
                newChat.id = chat.id
                newChat.chatName = chat.chatName
                newChat.lastMessage = chat.lastMessage
                newChat.timestamp = chat.timestamp
                for(user in chat.usersInChat){
                    newChat.usersInChat.add(user)
                }
                chatList.add(newChat)
            }
        }
        return chatList
    }
}