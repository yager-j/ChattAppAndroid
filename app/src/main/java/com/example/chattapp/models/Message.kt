package com.example.chattapp.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Message(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var sender: String = "A0CC5F6F-E5E1-461F-A737-E373C8F30E34",
    var text: String = "",
    var timestamp: Date = Date()
) : RealmObject() {
    override fun toString(): String {
        return "Message(id='$id', sender='$sender', text='$text', timestamp=$timestamp)"
    }
}