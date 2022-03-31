package com.example.chattapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class Chat(@field:ColumnInfo(name = "userID") var userID: Int) {

    @PrimaryKey(autoGenerate = true)
    var chatId = 0

    override fun toString(): String {
        return "Chat(userID=$userID, chatId=$chatId)"
    }
}
