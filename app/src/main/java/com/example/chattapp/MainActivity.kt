package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityMainBinding
import com.example.chattapp.firebase.FirestoreChatDao
import com.example.chattapp.firebase.FirestoreContactDao
import com.example.chattapp.firebase.FirestoreUserDao
import com.example.chattapp.models.Chat
import com.example.chattapp.models.Contact
import com.example.chattapp.realm.ContactDao
import com.example.chattapp.realm.UserDao
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration



class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding
    private lateinit var userDao: UserDao
    private lateinit var contactDao: ContactDao
    private lateinit var firestoreContactDao: FirestoreContactDao
    private lateinit var firestoreChatDao: FirestoreChatDao
    private lateinit var realmListener: RealmChangeListener<Realm>
    private lateinit var contactsList: ArrayList<Contact>

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        //creates or initializes the database
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("chatAppDB")
            .allowWritesOnUiThread(true)
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)

        userDao = UserDao()
        contactDao = ContactDao()
        firestoreContactDao = FirestoreContactDao()
        firestoreChatDao = FirestoreChatDao()
        firestoreChatDao.firestoreListener(this)
        FirestoreUserDao.loadUsers()

        sharedPrefsSetup()

        if (!UserManager.loadUserLogin()){
            println("data not loaded")
            val toLogin = Intent(this, LoginScreen::class.java)
            toLogin.putExtra("loginPressed", true)
            startActivity(toLogin)
        }

        //creates and add a listener to database to update everytime new items are added
        realmListener = RealmChangeListener {

            loadList()
        }
        userDao.db.addChangeListener(realmListener)

        binder.addUserBtn.setOnClickListener {
            val intent = Intent(this, NewChatActivity::class.java)
            startActivity(intent)
            //DialogMaker.createChat(this, contactDao, firestoreContactDao)
        }

        binder.buttonLogin.setOnClickListener{
            if (binder.buttonLogin.text == resources.getString(R.string.login)) {
                val toLogin = Intent(this, LoginScreen::class.java)
                toLogin.putExtra("loginPressed", true)
                startActivity(toLogin)
            } else {
                val popupMenu = PopupMenu(this, it)
                popupMenu.menuInflater.inflate(R.menu.menu_profile_options, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.item_1 -> print("item 1 pressed")
                        R.id.item_logout -> {
                            userDao.logOutUser()
                            updateView()
                        }
                    }
                    false
                }
                popupMenu.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("LoginUser", "updated.")
        updateView()
    }

    /**
     * loads the user list in the recycler view
     */
    fun loadList(chatList: ArrayList<Chat>) {

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binder.chatsList.layoutManager = layoutManager
        val adapter = ChatAdapter(chatList,this, { position -> onListItemClick(chatList[position])},{ position -> onListItemLongClick(position)})
        binder.chatsList.adapter = adapter

    }

    private fun onListItemClick(chat: Chat) {

        Toast.makeText(this, "click detected position $position", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatID", chat.id)
        intent.putExtra("chatName", chat.chatName)
        startActivity(intent)
    }

    private fun onListItemLongClick(position: Int){

        val id = contactsList[position].id
        contactDao.deleteContact(id)
        firestoreContactDao.deleteContact(id)

    }

    fun sharedPrefsSetup(){

        val sp = getSharedPreferences("com.example.chattapp.MyPrefs", MODE_PRIVATE)
        UserManager.sharedPrefsSetup(sp)
    }

    override fun onResume() {
        super.onResume()
        firestoreChatDao.firestoreListener(this)
    private fun updateView() {
        binder.buttonLogin.text = userDao.displayCurrentUser()
        if (binder.buttonLogin.text == "") {
            binder.buttonLogin.text = resources.getString(R.string.login)
        }
    }
}