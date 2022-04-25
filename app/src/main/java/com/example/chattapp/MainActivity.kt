package com.example.chattapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityMainBinding
import com.example.chattapp.firebase.FirestoreChatDao
import com.example.chattapp.firebase.FirestoreContactDao
import com.example.chattapp.firebase.FirestoreUserDao
import com.example.chattapp.models.Chat
import com.example.chattapp.models.Contact
import com.example.chattapp.realm.UserDao
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration


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

        //creates and add a listener to database to update everytime new items are added
        realmListener = RealmChangeListener {

            //loadList()
        }
        userDao.db.addChangeListener(realmListener)

        binder.newChatBtn.setOnClickListener {
            val intent = Intent(this, NewChatActivity::class.java)
            startActivity(intent)
            //DialogMaker.createChat(this, contactDao, firestoreContactDao)
        }

        binder.buttonLogin.setOnClickListener {
            if (UserManager.currentUser == null) {
                val toLogin = Intent(this, LoginScreen::class.java)
                toLogin.putExtra("loginPressed", true)
                startActivity(toLogin)
            } else {
                val popupMenu = PopupMenu(this, it)
                popupMenu.menuInflater.inflate(R.menu.menu_profile_options, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.item_1 -> {
                            Log.d("login", "............................First option pressed")
                        }
                        R.id.item_logout -> {
                            Log.d("login", "............................Log out now.")
                            UserManager.logOutUser(UserManager.currentUser!!.id)
                            //load login screen activity
                            updateView()
                        }
                    }
                    false
                }
                popupMenu.show()
            }
        }
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

        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("chatID", chat.id)
        intent.putExtra("chatName", chat.chatName)
        startActivity(intent)
    }

    private fun onListItemLongClick(position: Int) {

        val id = contactsList[position].id
        //contactDao.deleteContact(id)
        firestoreContactDao.deleteContact(id)

    }

    private fun sharedPrefsSetup() {

        val sp = getSharedPreferences("com.example.chattapp.MyPrefs", MODE_PRIVATE)
        UserManager.sharedPrefsSetup(sp)
    }

    override fun onResume() {
        super.onResume()
        Log.d("login", "............................is this happening?")
        UserManager.logInUser()
        updateView()
        //firestoreChatDao.firestoreListener(this)
    }

    private fun updateView() {
        Log.d("login", "............................Hello ${UserManager.currentUser}")
        if (UserManager.currentUser != null) {
            binder.buttonLogin.text = UserManager.currentUser!!.username
        } else {
            binder.buttonLogin.text = resources.getString(R.string.login)
        }
        firestoreChatDao.firestoreListener(this)
    }
}