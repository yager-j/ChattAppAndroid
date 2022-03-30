package com.example.chattapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataDao(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int): SQLiteOpenHelper(context, name, factory, version) {

    val TABLE_USERS = "users"
    val TABLE_CHATS = "chats"
    val TABLE_MESSAGES = "messages"

    //USERS:
    val COL_USER_ID = "userID"
    val COL_NAME = "name"

    //CHATS:
    val COL_CHAT_ID = "chatID"
    // + COL_USER_ID

    //MESSAGES:
    val COL_TEXT_ID = "textID"
    val COL_TEXT = "text"
    val COL_TIME = "timeStamp"
    // + COL_USER_ID
    // + COL_CHAT_ID

    constructor(context: Context?) : this(context, "ChatDataBase", null, 1)

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatementUser = "CREATE TABLE $TABLE_USERS ($COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT)"
        val createTableStatementChat = "CREATE TABLE $TABLE_CHATS ($COL_CHAT_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_USER_ID INTEGER, FOREIGN KEY($COL_USER_ID) REFERENCES $TABLE_USERS($COL_USER_ID))"
        val createTableStatementMessage = "CREATE TABLE $TABLE_MESSAGES ($COL_TEXT_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TEXT TEXT, $COL_TIME TEXT, $COL_USER_ID INTEGER, $COL_CHAT_ID INTEGER, FOREIGN KEY($COL_USER_ID) REFERENCES $TABLE_USERS($COL_USER_ID), FOREIGN KEY($COL_CHAT_ID) REFERENCES $TABLE_CHATS($COL_CHAT_ID))"

        db?.execSQL(createTableStatementUser)
        db?.execSQL(createTableStatementChat)
        db?.execSQL(createTableStatementMessage)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun addChat(chat: Chat) : Boolean {
        return true
    }
}