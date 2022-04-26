package com.example.chattapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chattapp.databinding.ActivityMainBinding
import com.example.chattapp.firebase.FirestoreChatDao
import com.example.chattapp.firebase.FirestoreContactDao
import com.example.chattapp.firebase.FirestoreUserDao
import com.example.chattapp.firebase.ImageManager
import com.example.chattapp.models.Chat
import com.example.chattapp.realm.ChatDao
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {

    private lateinit var binder: ActivityMainBinding
    private lateinit var fireStoreContactDao: FirestoreContactDao
    private lateinit var fireStoreChatDao: FirestoreChatDao

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

        loadList(ChatDao.getChats())

        fireStoreContactDao = FirestoreContactDao()
        fireStoreChatDao = FirestoreChatDao()
        fireStoreChatDao.firestoreListener(this)
        FirestoreUserDao.loadUsers()

        sharedPrefsSetup()

        binder.newChatBtn.setOnClickListener {
            val intent = Intent(this, NewChatActivity::class.java)
            startActivity(intent)
        }

        binder.buttonLogin.setOnClickListener {
            if (UserManager.currentUser == null) {
                val toLogin = Intent(this, LoginScreen::class.java)
                toLogin.putExtra("loginPressed", true)
                startActivity(toLogin)
            } else {
                val popupMenu = PopupMenu(this, it)
                popupMenu.menuInflater.inflate(R.menu.menu_profile_options, popupMenu.menu)
                popupMenu.menu[0].title = "${UserManager.currentUser!!.first_name} ${UserManager.currentUser!!.last_name}"
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.item_change_profile -> {
                            imageChooser()
                        }
                        R.id.item_change_password -> {
                            val toLogin = Intent(this, LoginScreen::class.java)
                            toLogin.putExtra("changePassword", true)
                            startActivity(toLogin)
                        }
                        R.id.item_logout -> {
                            UserManager.logOutUser(UserManager.currentUser!!.id)
                            reloadUser()
                            updateView()
                        }
                    }
                    false
                }
                popupMenu.show()
            }
        }
    }

    private fun imageChooser() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 3)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null){
            var selectedImage = data.data
            binder.imgProfileCurrent.setImageURI(selectedImage)
            if (selectedImage != null) {
                ImageManager.saveImage(selectedImage)
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
//        val id = contactsList[position].id
//        contactDao.deleteContact(id)
//        fireStoreContactDao.deleteContact(id)
    }

    private fun sharedPrefsSetup() {
        val sp = getSharedPreferences("com.example.chattapp.MyPrefs", MODE_PRIVATE)
        UserManager.sharedPrefsSetup(sp)
    }

    override fun onResume() {
        super.onResume()
        reloadUser()
        updateView()
        loadProfilePic()
    }

    private fun loadProfilePic() {
        if(UserManager.currentUser != null){
            val imageRef = ImageManager.getImageURL(UserManager.currentUser!!.id)
            imageRef.downloadUrl.addOnSuccessListener {
                Glide.with(this).load(it).into(binder.imgProfileCurrent)
            }.addOnFailureListener {
                println("Failed to load image")
            }
        }
    }

    private fun reloadUser() {
        UserManager.logInUser()
        if (UserManager.currentUser == null) {
            RegLogSelection.selectOption( this) { isLogin ->
                val toLogin = Intent(this, LoginScreen::class.java)
                toLogin.putExtra("loginPressed", isLogin)
                startActivity(toLogin)
            }
        }
    }

    private fun updateView() {
        if (UserManager.currentUser != null) {
            binder.buttonLogin.text = UserManager.currentUser!!.username
        } else {
            binder.buttonLogin.text = resources.getString(R.string.login)
        }
        fireStoreChatDao.firestoreListener(this)
    }
}