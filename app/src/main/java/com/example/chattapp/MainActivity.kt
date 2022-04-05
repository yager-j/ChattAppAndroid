package com.example.chattapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattapp.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmConfiguration

private lateinit var binder: ActivityMainBinding
private lateinit var userDao: UserDao
private lateinit var realmListener: RealmChangeListener<Realm>

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binder.root)

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("chatAppDB")
            .allowWritesOnUiThread(true)
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)

        userDao = UserDao()
        loadList()

        realmListener = RealmChangeListener {

            loadList()
        }
        userDao.db.addChangeListener(realmListener)

        binder.addUserBtn.setOnClickListener {
            userDao.addUser()
        }

    }

    private fun loadList() {

        binder.chatsList.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter(userDao.getUsers()) { position -> onListItemClick(position) }
        binder.chatsList.adapter = adapter

    }

    private fun onListItemClick(position: Int) {

        println(position)

    }
}