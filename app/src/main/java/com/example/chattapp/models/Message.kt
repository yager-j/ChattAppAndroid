package com.example.chattapp.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Message(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var sender: String = "",
    var text: String = "",
    var timestamp: Date = Date(),
    var referenceChatId: String = ""
) : RealmObject() {
    override fun toString(): String {
        return "Message(id='$id', sender='$sender', text='$text', timestamp=$timestamp, referenceChatId:$referenceChatId)"
    }
}