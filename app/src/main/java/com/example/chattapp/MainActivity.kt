package com.example.chattapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration


private lateinit var binder: ActivityMainBinding
private lateinit var userDao: UserDao
private lateinit var contactDao: ContactDao
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
        loadList()

//creates and add a listener to database to update everytime new items are added
        realmListener = RealmChangeListener {

            loadList()
        }
        userDao.db.addChangeListener(realmListener)

        binder.addUserBtn.setOnClickListener {
            DialogMaker.createChat(this, contactDao)
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
        userDao.debugShowUser("Qualthorn")
        userDao.debugShowUser("Hej")
        updateView()
    }

    /**
     * loads the user list in the recycler view
     */
    private fun loadList() {

        binder.chatsList.layoutManager = LinearLayoutManager(this)
        contactsList = contactDao.getContacts()
        val adapter = MyAdapter((contactsList),{ position -> onListItemClick(position)},{ position -> onListItemLongClick(position)})
        binder.chatsList.adapter = adapter

    }

    private fun onListItemClick(position: Int) {

        Toast.makeText(this, "click detected position $position", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ChatActivity::class.java)
        startActivity(intent)
    }

    private fun onListItemLongClick(position: Int){
        val id = contactsList[position].id
        contactDao.deleteContact(id)
    }

    private fun updateView() {
        binder.buttonLogin.text = userDao.displayCurrentUser()
        if (binder.buttonLogin.text == "") {
            binder.buttonLogin.text = resources.getString(R.string.login)
        }
    }
}