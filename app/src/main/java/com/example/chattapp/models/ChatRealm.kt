package com.example.chattapp.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ChatRealm  (

    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var usersInChat: RealmList<String> = RealmList<String>(),
    var chatName: String = "",
    var lastMessage: String = "",
    var timestamp: Date = Date()) : RealmObject() {

}