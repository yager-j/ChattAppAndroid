package com.example.chattapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey


@Entity
data class Message(@field:ColumnInfo(name = "text") var message: String, @field:ColumnInfo(name = "timestamp") var timeStamp: String) {
//
//    @PrimaryKey(autoGenerate = true)
//    var messageId = 0
//
//    @Entity(foreignKeys = @ForeignKey(entity = Chat.class,
//        parentColumns = "chatId",
//        childColumns = "chatId",
//        onDelete = CASCADE))
//
//}

    @Entity
    data class Message(
        @PrimaryKey(autoGenerate = true)
        val uid: Long,
        val firstName: String = "",
        val lastName: String = "",
        val email: String = ""
    )

    @Entity(
        foreignKeys = arrayOf(
            ForeignKey(
                entity = Message::class,
                parentColumns = arrayOf("uid"),
                childColumns = arrayOf("ownerId"),
                onDelete = ForeignKey.CASCADE
            )
        )
    )
    data class Pet(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val ownerId: Long,
        val name: String
    )

}
