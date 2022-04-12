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

        val adapter = NewChatAdapter(list, {position ->  onListItemClick(list[position])})
        binder.recyclerviewNewChat.adapter = adapter
        binder.recyclerviewNewChat.layoutManager = LinearLayoutManager(this)
        binder.recyclerviewNewChat.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun onListItemClick(user: User){
        Toast.makeText(this, "Clicked on: ${user.userName}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ChatActivity::class.java)
        val userList = ArrayList<String>()
        userList.add(user.userName)

        intent.putExtra("userList", userList)
        startActivity(intent)
    }
}