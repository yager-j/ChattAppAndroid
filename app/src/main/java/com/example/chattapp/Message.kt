package com.example.chattapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Message(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var sender: String = "dave",
    @Required
    var receiver: String = "",
    var text: String = "",
    var timestamp: Date = Date()
) : RealmObject() {
    override fun toString(): String {
        return "Message(id='$id', sender='$sender', receiver='$receiver', text='$text', timestamp=$timestamp)"
    }
}