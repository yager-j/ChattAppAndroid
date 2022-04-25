package com.example.chattapp

import android.content.SharedPreferences
import android.util.Log
import com.example.chattapp.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.realm.Realm


object UserManager {

    var currentUser: User? = null
    val USERNAME_LOGIN_KEY = "userName"
    val MAIL_LOGIN_KEY = "eMail"
    val PASSWORD_LOGIN_KEY = "Password"

    private val USERS_COLLECTION = "users"

    //TEMP
    private val ID_KEY = "id"
    private val NAME_KEY = "first_name"
    private val LASTNAME_KEY = "last_name"
    private val USERNAME_KEY = "user_name"
    private val EMAIL_KEY = "e_mail"
    private val PASSWORD_KEY = "password"

    private lateinit var sp: SharedPreferences
    val realmDB: Realm = Realm.getDefaultInstance()

    fun sharedPrefsSetup(sp: SharedPreferences){

        this.sp = sp

    }

    fun saveUserLogin(userName: String, pw: String) {
        val editor = sp.edit()
        if (userName.contains("@")){
            editor.putString(USERNAME_LOGIN_KEY, userName)
        }
        editor.putString(USERNAME_LOGIN_KEY, userName)
        editor.putString(PASSWORD_LOGIN_KEY, pw)
        editor.apply()
    }

    fun loadUserLogin(): Boolean {

        val loadUsername = sp.getString(USERNAME_LOGIN_KEY, "null")
        val loadMail = sp.getString(MAIL_LOGIN_KEY, "null")
        val loadPw = sp.getString(PASSWORD_LOGIN_KEY, "null")

        if (loadUsername != "null" && loadPw != "null" || loadMail != "null" && loadPw != "null") {

            val firebaseDB = FirebaseFirestore.getInstance()

            val query: Query = if (loadMail != "null")
                firebaseDB.collection(USERS_COLLECTION).whereEqualTo(EMAIL_KEY, loadMail)
            else
                firebaseDB.collection(USERS_COLLECTION).whereEqualTo(USERNAME_KEY, loadUsername)

            query.get().addOnSuccessListener {
                    if (it != null) {
                        for (doc in it) {
                            val user = User()
                            user.id = doc.getString(ID_KEY)!!
                            user.first_name = doc.getString(NAME_KEY)!!
                            user.last_name = doc.getString(LASTNAME_KEY)!!
                            user.username = doc.getString(USERNAME_KEY)!!
                            user.email = doc.getString(EMAIL_KEY)!!
                            user.password = doc.getString(PASSWORD_KEY)!!
                            currentUser = user
                            println("VICTORY")
                            Log.d("login", "............................VICTORY")
                        }
                    }
                }
                .addOnFailureListener {
                    println("DANG")
                    Log.d("login", "............................DANG")
                }
            return true
        } else {
            return false
        }

    }

    fun saveLoggedInUser(user: User){
        realmDB.executeTransaction {
            val newUser = User().apply {
                id = user.id
                name = user.name
                lastName = user.lastName
                userName = user.userName
                eMail = user.eMail
                password = user.password
            }
            it.insert(newUser)
        }
    }

    fun logInUser(){
        realmDB.executeTransaction {
            val user = realmDB.where(User::class.java).findFirst()
            currentUser = user
            Log.d("login", "............................Currently logged in: $user")
        }
    }

    fun logOutUser(userId: String){
        currentUser = null
        realmDB.executeTransaction {
            val user = realmDB.where(User::class.java).equalTo("id", userId).findFirst()
            user?.deleteFromRealm()
        }
    }
}