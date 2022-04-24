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

    private var selectedUsers = arrayListOf<User>()
    private var selectedUsersId = arrayListOf<String>()
    private var selectedUsersName = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        FirestoreUserDao.firestoreUserListener(this)

        //Start new chat
        binder.createChatButton.setOnClickListener {

            if(selectedUsers.isNotEmpty()) {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("userIdList", selectedUsersId)
                intent.putExtra("userNameList", selectedUsersName)
                intent.putExtra("chatName", createChatName())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Select a User to create a chat", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createChatName(): String {
        var chatName = ""
        for(name in selectedUsersName){
            chatName += "$name, "
        }
        chatName = chatName.dropLast(2)
        return chatName
    }

    fun showUsers(list: ArrayList<User>){

        val adapter = NewChatAdapter(list, {position ->  onListItemClick(list[position])})
        binder.recyclerviewNewChat.adapter = adapter
        binder.recyclerviewNewChat.layoutManager = LinearLayoutManager(this)
        binder.recyclerviewNewChat.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun onListItemClick(user: User){
        if(selectedUsers.contains(user)){
            selectedUsers.remove(user)
            selectedUsersId.remove(user.id)
            selectedUsersName.remove(user.userName)
        } else {
            selectedUsers.add(user)
            selectedUsersId.add(user.id)
            selectedUsersName.add(user.userName)
        }
        Toast.makeText(this, "Selected users: $selectedUsers", Toast.LENGTH_SHORT).show()
    }
}