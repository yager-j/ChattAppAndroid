package com.example.chattapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityNewChatBinding
import com.example.chattapp.firebase.FirestoreUserDao
import com.example.chattapp.models.User


class NewChatActivity : AppCompatActivity() {

    private lateinit var binder: ActivityNewChatBinding
    private lateinit var firestoreUserDao: FirestoreUserDao

    private var selectedUsers = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        firestoreUserDao = FirestoreUserDao(this)

        //Start new chat
        binder.createChatButton.setOnClickListener {

            if(selectedUsers.isNotEmpty()) {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("userList", selectedUsers)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Select a User to create a chat", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun showUsers(list: ArrayList<User>){

        val adapter = NewChatAdapter(list, {position ->  onListItemClick(list[position])})
        binder.recyclerviewNewChat.adapter = adapter
        binder.recyclerviewNewChat.layoutManager = LinearLayoutManager(this)
        binder.recyclerviewNewChat.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun onListItemClick(user: User){
        if(selectedUsers.contains(user.id)){
            selectedUsers.remove(user.id)
        } else {
            selectedUsers.add(user.id)
        }
        Toast.makeText(this, "Selected users: $selectedUsers", Toast.LENGTH_SHORT).show()
    }
}