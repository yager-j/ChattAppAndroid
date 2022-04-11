package com.example.chattapp


import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.chattapp.databinding.ActivityNewChatBinding

private lateinit var binder: ActivityNewChatBinding
private lateinit var firestoreUserDao: FirestoreUserDao

class NewChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        firestoreUserDao = FirestoreUserDao(this)

    }

    fun showUsers(list: ArrayList<User>){
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        binder.listViewNewChat.adapter = adapter
    }
}