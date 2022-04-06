package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration

private lateinit var binder: ActivityMainBinding
private lateinit var userDao: UserDao
private lateinit var contactDao: ContactDao
private lateinit var firestoreContactDao: FirestoreContactDao
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
        loadList()

//creates and add a listener to database to update everytime new items are added
        realmListener = RealmChangeListener {

            loadList()
        }
        userDao.db.addChangeListener(realmListener)

        binder.addUserBtn.setOnClickListener {
            DialogMaker.createChat(this, contactDao, firestoreContactDao)
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
}