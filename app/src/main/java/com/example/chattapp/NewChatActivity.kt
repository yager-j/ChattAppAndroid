package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityNewChatBinding
import com.example.chattapp.firebase.FirestoreUserDao
import com.example.chattapp.models.User

class NewChatActivity : AppCompatActivity() {

    private lateinit var binder: ActivityNewChatBinding

    private lateinit var firestoreUserDao: FirestoreUserDao

    private var selectedUsers = arrayListOf<User>()
    private var selectedUsersId = arrayListOf<String>()
    private var selectedUsersName = arrayListOf<String>()
    private var searchTerm = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binder.root)

        FirestoreUserDao.firestoreUserListener(this)

        //Search for user
        binder.searchUserEdittext.addTextChangedListener {
            val searchResult = searchUsers(it)
            showUsers(searchResult)
        }
        
        //Start new chat
        binder.createChatButton.setOnClickListener {

            if(selectedUsers.isNotEmpty()) {
                val intent = Intent(this, ChatActivity::class.java)
                selectedUsersId.add(UserManager.currentUser!!.id)
                intent.putExtra("userIdList", selectedUsersId)
                selectedUsersName.add(UserManager.currentUser!!.username)
                intent.putExtra("userNameList", selectedUsersName)
                intent.putExtra("chatName", createChatName())
                println(selectedUsersId.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "Select a User to create a chat", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchUsers(it: Editable?): ArrayList<User> {
        var searchResult = arrayListOf<User>()
        searchTerm = it.toString()
        if (searchTerm.isNotEmpty()){
            for(user in FirestoreUserDao.userList){
                if(user.username.contains(searchTerm, ignoreCase = true)){
                    searchResult.add(user)
                }
            }
        } else {
            searchResult = FirestoreUserDao.userList
        }
        return searchResult
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

        val adapter = NewChatAdapter(list, selectedUsersName, {position ->  onListItemClick(list[position])})
        binder.recyclerviewNewChat.adapter = adapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binder.recyclerviewNewChat.layoutManager = layoutManager

    }

    private fun onListItemClick(user: User){
        if(selectedUsers.contains(user)){
            selectedUsers.remove(user)
            selectedUsersId.remove(user.id)
            selectedUsersName.remove(user.username)
        } else {
            selectedUsers.add(user)
            selectedUsersId.add(user.id)
            selectedUsersName.add(user.username)
        }
    }
}