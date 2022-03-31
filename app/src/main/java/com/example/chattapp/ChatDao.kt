package com.example.chattapp

import androidx.room.*

@Dao
interface ChatDao {
    @get:Query("SELECT * FROM Chat")
    val all: List<Any?>?

    @Update
    fun updateUser(user: Chat?)

    @Insert
    fun insertUser(user: Chat?)

    @Delete
    fun deleteUser(user: Chat?)
}