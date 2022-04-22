package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityMainBinding
import com.example.chattapp.firebase.FirestoreChatDao
import com.example.chattapp.firebase.FirestoreContactDao
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

        sharedPrefsSetup()

        if (!UserManager.loadUserLogin()){
            println("data not loaded")
            val toLogin = Intent(this, LoginScreen::class.java)
            toLogin.putExtra("loginPressed", true)
            startActivity(toLogin)
        }

        //creates and add a listener to database to update everytime new items are added
        realmListener = RealmChangeListener {

            //loadList()
        }
        userDao.db.addChangeListener(realmListener)

        binder.addUserBtn.setOnClickListener {
            val intent = Intent(this, NewChatActivity::class.java)
            startActivity(intent)
            //DialogMaker.createChat(this, contactDao, firestoreContactDao)
        }

        binder.buttonLogin.setOnClickListener{
            val toLogin = Intent(this, LoginScreen::class.java)
            toLogin.putExtra("loginPressed", true)
            startActivity(toLogin)
        }

    }

    /**
     * loads the user list in the recycler view
     */
    fun loadList(chatList: ArrayList<Chat>) {

        binder.chatsList.layoutManager = LinearLayoutManager(this)
        val adapter = ChatAdapter(chatList,this, { position -> onListItemClick(chatList[position])},{ position -> onListItemLongClick(position)})
        binder.chatsList.adapter = adapter
        //binder.chatsList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }

    private fun onListItemClick(chat: Chat) {

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatID", chat.id)
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
    }
}